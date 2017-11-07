package com.github.dataflow.node.model.zookeeper;

import com.github.dataflow.common.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注册instance到zk
 *
 * @author kevin
 * @date 2017-05-30 1:29 AM.
 */
@Component
public class InstanceRegister {
    private Logger logger = LoggerFactory.getLogger(InstanceRegister.class);

    @Autowired
    private ZookeeperClient zookeeperClient;

    public void registerToZookeeper(String instanceName, String data) {
        String path = Constants.DEFAULT_INSTANCE_PATH + "/" + instanceName;
        zookeeperClient.createNodePathIfNotExists(Constants.DEFAULT_INSTANCE_PATH);
        zookeeperClient.createEphemeral(path, data);
        logger.info("Instance [{}] register to ZK cluster, data : {}", instanceName, data);
    }

    public void unregisterToZookeeper(String instanceName) {
        String path = Constants.DEFAULT_INSTANCE_PATH + "/" + instanceName;
        zookeeperClient.delete(path);
        logger.info("Instance [{}] unregister from ZK cluster.", instanceName);
    }

    public boolean exists(String instanceName) {
        String path = Constants.DEFAULT_INSTANCE_PATH + "/" + instanceName;
        return zookeeperClient.exists(path);
    }
}
