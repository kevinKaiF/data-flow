package com.github.dataflow.node.model.zookeeper;

import com.alibaba.otter.canal.common.zookeeper.ZkClientx;
import org.I0Itec.zkclient.IZkChildListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
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

    private ZkClientx zkClientx;

    public void afterPropertiesSet() throws Exception {
        zkClientx = ZkClientx.getZkClient(zookeeperAddresses);
    }

    public void createEphemeral(String path, Object data) {
        zkClientx.createEphemeral(path, data);
    }

    public void createEphemeral(String path) {
        zkClientx.createEphemeral(path);
    }

    public void delete(String path) {
        zkClientx.delete(path);
    }

    public void subscribeChildChanges(String path, IZkChildListener listener) {
        zkClientx.subscribeChildChanges(path, listener);
    }

    public List<String> getChildren(String path) {
        if (zkClientx.exists(path)) {
            return zkClientx.getChildren(path);
        } else {
            return Collections.emptyList();
        }
    }

    public void createNodePathIfNotExists(String path) {
        boolean exists = zkClientx.exists(path);
        if (!exists) {
            zkClientx.createPersistent(path, null, true);
        }
    }
}
