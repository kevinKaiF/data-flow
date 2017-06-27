package com.github.dataflow.dashboard.router;

import com.github.dataflow.common.utils.IPUtil;

import java.util.List;

/**
 * hash
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/14
 */
public class HashRouter extends AbstractRouter {

    @Override
    protected String doNext(List<String> children, String ipAddress) {
        int hashCode = IPUtil.valueOf(ipAddress);
        return children.get(hashCode % children.size());
    }
}
