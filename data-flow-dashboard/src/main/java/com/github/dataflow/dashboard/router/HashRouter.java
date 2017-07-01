package com.github.dataflow.dashboard.router;

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
    protected String doNext(List<String> children, String name) {
        int hashCode = name.hashCode();
        return children.get(hashCode % children.size());
    }
}
