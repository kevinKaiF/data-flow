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
     * 获取下一个producer节点
     *
     * @param name
     * @return
     */
    String nextProducer(String name);

    /**
     * 获取下一个consumer节点
     *
     * @param name
     * @return
     */
    String nextConsumer(String name);
}
