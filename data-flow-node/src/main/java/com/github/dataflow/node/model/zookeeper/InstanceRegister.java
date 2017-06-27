package com.github.dataflow.node.model.zookeeper;

import com.github.dataflow.common.utils.Constants;
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
    @Autowired
    private ZookeeperClient zookeeperClient;

    public void registerToZookeeper(String instanceName, String data) {
        String path = Constants.DEFAULT_INSTANCE_PATH + "/" + instanceName;
        zookeeperClient.createNodePathIfNotExists(Constants.DEFAULT_INSTANCE_PATH);
        zookeeperClient.createEphemeral(path, data);
    }

    public void unregisterToZookeeper(String instanceName) {
        String path = Constants.DEFAULT_INSTANCE_PATH + "/" + instanceName;
        zookeeperClient.delete(path);
    }
}
