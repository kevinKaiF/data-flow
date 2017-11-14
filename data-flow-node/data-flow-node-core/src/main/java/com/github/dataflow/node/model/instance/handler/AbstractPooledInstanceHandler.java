package com.github.dataflow.node.model.instance.handler;

/**
 * 轮询拉取数据的实例处理器
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/5
 */
public abstract class AbstractPooledInstanceHandler extends AbstractInstanceHandler {
    /**
     * 消费获取时延
     */
    protected final long DEFAULT_TIMEOUT = 2000;

    /**
     * 消费获取时间间隔
     */
    protected final long DEFAULT_PERIOD  = 100;
}
