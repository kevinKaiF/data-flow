package com.github.dataflow.dashboard.router;

/**
 * node负载均衡策略
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/14
 */
public interface Router {

    /**
     * 获取下一个节点
     *
     * @param ipAddress
     * @return
     */
    String next(String ipAddress);
}
