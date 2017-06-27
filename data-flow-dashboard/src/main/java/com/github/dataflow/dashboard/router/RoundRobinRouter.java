package com.github.dataflow.dashboard.router;

import java.util.List;

/**
 * 轮询
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/14
 */
public class RoundRobinRouter extends AbstractRouter {
    private volatile int index = 0;

    @Override
    protected synchronized String doNext(List<String> children, String ipAddress) {
        if (index >= children.size()) {
            index = 0;
        }

        return children.get(index++ % children.size());
    }
}
