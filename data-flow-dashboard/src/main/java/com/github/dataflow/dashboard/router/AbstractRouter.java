package com.github.dataflow.dashboard.router;

import com.github.dataflow.common.utils.Constants;
import com.github.dataflow.dashboard.zookeeper.ZookeeperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/14
 */
public abstract class AbstractRouter implements Router {
    @Autowired
    private ZookeeperClient zookeeperClient;

    protected List<String> getProducerNodes() {
        return zookeeperClient.getChildren(Constants.DEFAULT_PRODUCER_NODE_PATH);
    }

    protected List<String> getConsumerNodes() {
        return zookeeperClient.getChildren(Constants.DEFAULT_CONSUMER_NODE_PATH);
    }

    @Override
    public String nextProducer(String name) {
        List<String> children = getProducerNodes();
        if (CollectionUtils.isEmpty(children)) {
            return null;
        } else {
            return doNext(children, name);
        }
    }

    protected abstract String doNext(List<String> children, String name);

    @Override
    public String nextConsumer(String name) {
        List<String> children = getConsumerNodes();
        if (CollectionUtils.isEmpty(children)) {
            return null;
        } else {
            return doNext(children, name);
        }
    }

}
