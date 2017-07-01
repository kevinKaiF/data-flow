package com.github.dataflow.dashboard.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author kevin
 * @date 2017-05-28 4:29 PM.
 */
@Component
public class ZookeeperClient implements InitializingBean {
    /**
     * zk集群地址
     */
    @Value("${node.zookeeper.addresses}")
    private String zookeeperAddresses;

    private ZkClient zkClient;

    public void afterPropertiesSet() throws Exception {
        zkClient = new ZkClient(zookeeperAddresses);
    }

    public List<String> getChildren(String path) {
        return zkClient.getChildren(path);
    }

    public boolean exists(String nodeParentPath) {
        return zkClient.exists(nodeParentPath);
    }
}
