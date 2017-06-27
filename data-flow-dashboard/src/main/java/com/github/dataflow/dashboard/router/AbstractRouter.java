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

    protected List<String> getNodes() {
        return zookeeperClient.getChildren(Constants.DEFAULT_NODE_PATH);
    }

    @Override
    public String next(String ipAddress) {
        List<String> children = getNodes();
        if (CollectionUtils.isEmpty(children)) {
            return null;
        } else {
            return doNext(children, ipAddress);
        }
    }

    protected abstract String doNext(List<String> children, String ipAddress);
}
