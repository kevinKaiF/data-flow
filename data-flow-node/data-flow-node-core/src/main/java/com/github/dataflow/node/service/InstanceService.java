package com.github.dataflow.node.service;

import com.github.dataflow.common.utils.IPUtil;
import com.github.dataflow.core.exception.InstanceException;
import com.github.dataflow.core.instance.Instance;
import com.github.dataflow.core.instance.InstanceManager;
import com.github.dataflow.dubbo.common.enums.DataInstanceStatus;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataInstanceService;
import com.github.dataflow.node.exception.InstanceInvalidException;
import com.github.dataflow.node.model.instance.factory.InstanceFactory;
import com.github.dataflow.node.model.zookeeper.InstanceRegister;
import com.github.dataflow.node.model.zookeeper.NodeRegister;
import com.github.dataflow.sender.core.DataSenderManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author kevin
 * @date 2017-05-28 3:05 PM.
 */
@Service
public class InstanceService implements DisposableBean {
    private Logger logger = LoggerFactory.getLogger(InstanceService.class);

    @Value("${jetty.connector.port}")
    private int port;

    @Autowired
    private InstanceFactory instanceFactory;

    @Autowired
    private InstanceRegister instanceRegister;

    @Autowired
    private NodeRegister nodeRegister;

    @Autowired
    private DubboDataInstanceService dataInstanceService;

    public void start(DataInstance dataInstance) {
        validate(dataInstance);
        if (InstanceManager.get(dataInstance.getName()) != null) {
            throw new InstanceException("Instance [" + dataInstance.getName() + "] has existed, don't repeatedly create.");
        }
        Instance instance = instanceFactory.createInstance(dataInstance);
        if (!instance.isStart()) {
            instance.start();
            // 注册instance到zk
            instanceRegister.registerToZookeeper(instance.getName(), getLocalAddress());
            // 更新数据库
            dataInstance.setStatus(DataInstanceStatus.START.getStatus());
            dataInstance.setNodePath(nodeRegister.getNodePath());
            updateDataInstance(dataInstance);
        } else {
            logger.warn("Instance [" + dataInstance.getName() + "] has started!");
        }
    }

    private void validate(DataInstance dataInstance) {
        if (dataInstance == null) {
            throw new InstanceInvalidException("DataInstance must not be null.");
        }

        if (StringUtils.isBlank(dataInstance.getName())) {
            throw new InstanceInvalidException("the name property of DataInstance must not be null.");
        }

        if (CollectionUtils.isEmpty(dataInstance.getDataOutputMappings())) {
            throw new InstanceInvalidException("the dataOutputMappings property of DataInstance must not be null.");
        }
    }

    /**
     * node节点死掉后重启instance
     * <p>
     * 优先更新instance状态到DB，因为zk上已经注册该instance节点，
     * 即使instance启动失败，instance节点在zk上被删除，可以准确
     * 地从数据库找到instance重启
     *
     * @param dataInstance
     */
    public void recovery(DataInstance dataInstance) {
        validate(dataInstance);
        if (InstanceManager.get(dataInstance.getName()) != null) {
            throw new InstanceException("Instance [" + dataInstance.getName() + "] has existed, don't repeatedly create.");
        }

        dataInstance.setNodePath(nodeRegister.getNodePath());
        // 优先更新数据库
        updateDataInstance(dataInstance);
        Instance instance = instanceFactory.createInstance(dataInstance);
        instance.start();
    }

    public void stop(DataInstance dataInstance) {
        if (StringUtils.isBlank(dataInstance.getName())) {
            throw new InstanceInvalidException("the name property of DataInstance must not be null.");
        }

        Instance instance = InstanceManager.get(dataInstance.getName());
        if (instance == null) {
            logger.warn("Instance [" + dataInstance.getName() + "] does not exist.");
        } else {
            if (instance.isStart()) {
                instance.stop();
            } else {
                logger.warn("Instance [" + dataInstance.getName() + "] is not started!");
            }
            InstanceManager.remove(dataInstance.getName());
        }
        // 更新数据库状态
        dataInstance.setStatus(DataInstanceStatus.STOP.getStatus());
        dataInstance.setNodePath(null);
        updateDataInstance(dataInstance);
        // 从zk上删除instance
        instanceRegister.unregisterToZookeeper(dataInstance.getName());
    }

    private void updateDataInstance(DataInstance dataInstance) {
        ServiceResult<Integer> serviceResult = dataInstanceService.update(dataInstance);
        if (!serviceResult.isSuccess()) {
            throw new InstanceException(serviceResult.getErrorMessage());
        }
    }

    public void destroy() throws Exception {
        Collection<Instance> instances = InstanceManager.getAllInstance();
        if (!CollectionUtils.isEmpty(instances)) {
            for (Instance instance : instances) {
                try {
                    instance.stop();
                    // 更新数据库状态
                    ServiceResult<Void> serviceResult = dataInstanceService.updateStatusByName(instance.getName(), DataInstanceStatus.STOP.getStatus());
                    if (!serviceResult.isSuccess()) {
                        throw new InstanceException(serviceResult.getErrorMessage());
                    }
                } catch (Exception e) {
                    logger.error("destroy Instance[name : {}] failure, detail : ",
                                 new Object[]{instance.getName(), e});
                }
            }

            DataSenderManager.clear();
        }
    }

    private String getLocalAddress() {
        return IPUtil.getLocalIp() + ":" + port;
    }

    public List<DataInstance> getDataInstanceByNodePath(String nodePath) {
        DataInstance dataInstance = new DataInstance();
        dataInstance.setNodePath(nodePath);
        PageSet pageSet = new PageSet(0, Integer.MAX_VALUE);
        ServiceResult<List<DataInstance>> serviceResult = dataInstanceService.findByCondition(dataInstance, pageSet);
        if (!serviceResult.isSuccess()) {
            throw new InstanceException(serviceResult.getErrorMessage());
        }

        List<DataInstance> result = serviceResult.getResult();
        if (CollectionUtils.isEmpty(result)) {
            logger.warn("result is empty when apply DubboDataInstanceService [findByCondition] method, params : [{}, {}].", dataInstance, pageSet);
        }

        return result;
    }
}
