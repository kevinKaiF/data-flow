package com.github.dataflow.node.model.zookeeper;

import com.github.dataflow.common.utils.Constants;
import com.github.dataflow.common.utils.IPUtil;
import com.github.dataflow.dubbo.common.enums.DataInstanceModel;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.node.exception.InstanceInvalidException;
import com.github.dataflow.node.model.alarm.AlarmService;
import com.github.dataflow.node.model.config.DataFlowContext;
import com.github.dataflow.node.service.InstanceService;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 注册本节点到zk
 *
 * @author kevin
 * @date 2017-05-28 4:12 PM.
 */
@Component
public class NodeRegister implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(NodeRegister.class);

    @Value("${jetty.connector.port}")
    private int port;

    @Value("${instance.recovery.retryTime:3}")
    private int instanceRecoveryRetryTime;

    @Value("${node.type:PRODUCER}")
    private String nodeType;

    /**
     * 缓存集群中所有的node
     * <p>
     * 缓存是因为zk的NodeChildrenChange事件只能通知子节点变化的parentPath，
     * 但是无法得知增加了什么节点，或者删除了什么节点
     */
    private volatile List<String> clusterNodes;

    @Autowired
    private ZookeeperClient zookeeperClient;

    @Autowired
    private InstanceRegister instanceRegister;

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private DataFlowContext dataFlowContext;

    /**
     * node注册到zk的路径
     */
    private String nodePath;

    public void afterPropertiesSet() throws Exception {
        registerToZookeeper();
    }

    private void registerToZookeeper() {
        nodePath = createNodePath();
        zookeeperClient.createNodePathIfNotExists(getNodeParentPath());
        zookeeperClient.createEphemeral(nodePath);
        clusterNodes = zookeeperClient.getChildren(getNodeParentPath());
        zookeeperClient.subscribeChildChanges(getNodeParentPath(), new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> children) throws Exception {
                List<String> oldClusterNodes = new ArrayList<>(clusterNodes);
                clusterNodes = children;
                oldClusterNodes.removeAll(children);
                // 监听到节点删除事件
                if (!CollectionUtils.isEmpty(oldClusterNodes) && zookeeperClient.exists(nodePath)) {
                    AlarmService alarmService = dataFlowContext.getAlarmService();
                    // 发送通知给负责人
                    alarmService.sendAlarm(Notice.NODE_RECOVERY.getSubject(), format(Notice.NODE_RECOVERY.getMessage(), getNodeName(), oldClusterNodes), false);
                    logger.warn("Node {} is dead, now recovery all dead Instance.", oldClusterNodes);
                    for (String deleteChild : oldClusterNodes) {
                        String nodePath = parentPath + "/" + deleteChild;
                        // 从db获取DataInstance
                        List<DataInstance> dataInstanceList = null;
                        try {
                            dataInstanceList = instanceService.getDataInstanceByNodePath(nodePath);
                        } catch (Exception e) {
                            logger.error("apply InstanceService [getDataInstanceByNodePath] method failure, params : {}", nodePath, e);
                            // 发送通知给负责人
                            alarmService.sendAlarm(Notice.GET_INSTANCES_FAILURE.getSubject(), format(Notice.GET_INSTANCES_FAILURE.getMessage(), getNodeName(), nodePath, exceptionToString(e)), false);
                        }

                        if (!CollectionUtils.isEmpty(dataInstanceList)) {
                            for (DataInstance dataInstance : dataInstanceList) {
                                Exception snatchInstanceRegistryException = null;
                                try {
                                    // 抢占式注册instance节点，如果抢占成功，则启动instance，否则抢占创建下个instance
                                    // 这个过程是抢占式获取锁的过程
                                    instanceRegister.registerToZookeeper(dataInstance.getName(), getNodeName());
                                } catch (Exception e) {
                                    snatchInstanceRegistryException = e;
                                    if (e instanceof ZkNodeExistsException) {
                                        logger.warn("snatch Instance [name : {}] failure", dataInstance.getName());
                                    } else {
                                        // 发送通知给负责人
                                        logger.error("snatch Instance [name : {}] caught exception, detail : {}", new Object[]{dataInstance.getName(), e});
                                        alarmService.sendAlarm(Notice.SNATCH_INSTANCE_FAILURE.getSubject(), format(Notice.SNATCH_INSTANCE_FAILURE.getMessage(), getNodeName(), dataInstance.getName(), exceptionToString(e)), false);
                                    }
                                }

                                if (snatchInstanceRegistryException == null) {
                                    Exception recoveryFailureException = null;
                                    try {
                                        resetDataInstanceNodePath(dataInstance, nodePath);
                                        instanceService.recovery(dataInstance);
                                        logger.info("recovery Instance [name : {}] successful.", dataInstance.getName());
                                    } catch (Exception e) {
                                        if (e instanceof InstanceInvalidException) {
                                            logger.error("DataInstance [name : {}] is invalid, detail : ", new Object[]{dataInstance.getName(), e});
                                            // 发送通知给负责人
                                            alarmService.sendAlarm(Notice.INVALID_INSTANCE.getSubject(), format(Notice.INVALID_INSTANCE.getMessage(), getNodeName(), dataInstance.getName()), false);
                                        } else {
                                            logger.warn("recovery Instance [name : {}] failure, now attempt to retry {} times.",
                                                        new Object[]{dataInstance.getName(), instanceRecoveryRetryTime});
                                            recoveryFailureException = e;
                                            for (int i = 0; i < instanceRecoveryRetryTime && recoveryFailureException != null; i++) {
                                                try {
                                                    instanceService.recovery(dataInstance);
                                                    logger.info("recovery Instance [name : {}] successful after retry {} times.",
                                                                new Object[]{dataInstance.getName(), i + 1});
                                                    recoveryFailureException = null;
                                                } catch (Exception ex) {
                                                    logger.warn("retry {} time to  recovery Instance [name : {}] failure.",
                                                                new Object[]{i + 1, dataInstance.getName()});
                                                    recoveryFailureException = ex;
                                                }
                                            }

                                            if (recoveryFailureException != null) {
                                                // 发送通知给负责人
                                                logger.error("recovery Instance [name : {}] failure after retry {} times.",
                                                             new Object[]{dataInstance.getName(), instanceRecoveryRetryTime});
                                                alarmService.sendAlarm(Notice.RECOVERY_INSTANCE_FAILURE.getSubject(), format(Notice.RECOVERY_INSTANCE_FAILURE.getMessage(), getNodeName(), dataInstance.getName(), instanceRecoveryRetryTime), false);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private String getNodeParentPath() {
        if (nodeType.equalsIgnoreCase(DataInstanceModel.PRODUCER.name())) {
            return Constants.DEFAULT_PRODUCER_NODE_PATH;
        } else {
            return Constants.DEFAULT_CONSUMER_NODE_PATH;
        }
    }

    public String getNodePath() {
        return nodePath;
    }

    private String createNodePath() {
        return getNodeParentPath() + "/" + getNodeName();
    }

    private static String format(String pattern, Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }

    private static String exceptionToString(Throwable e) {
        return ExceptionUtils.getFullStackTrace(e);
    }

    /**
     * 重置DataInstance所在的node的zk路径
     *
     * @param dataInstance
     * @param instancePath
     */
    private static void resetDataInstanceNodePath(DataInstance dataInstance, String instancePath) {
        dataInstance.setNodePath(instancePath);
    }

    private String getNodeName() {
        return IPUtil.getLocalIp() + ":" + port;
    }

    /**
     * 节点recovery通知
     */
    enum Notice {
        NODE_RECOVERY("node recovery", "[{0}] - {1}节点挂掉，正在重启各个实例"),
        GET_INSTANCES_FAILURE("get Instances failure", "[{0}] - 获取Instance[{1}]下所有失败，异常：{2}"),
        SNATCH_INSTANCE_FAILURE("snatch Instance failure", "[{0}] - 抢占创建Instance[{1}]失败，异常：{2}"),
        INVALID_INSTANCE("invalid Instance", "[{0}] - 无效的Instance[{1}]"),
        RECOVERY_INSTANCE_FAILURE("recovery Instance failure", "[{0}] - 尝试recovery Instance[{1}] {2}次失败");
        /**
         * 通知的主题
         */
        private String subject;

        /**
         * 通知的消息
         */
        private String message;

        Notice(String subject, String message) {
            this.subject = subject;
            this.message = message;
        }

        public String getSubject() {
            return subject;
        }

        public String getMessage() {
            return message;
        }
    }


}
