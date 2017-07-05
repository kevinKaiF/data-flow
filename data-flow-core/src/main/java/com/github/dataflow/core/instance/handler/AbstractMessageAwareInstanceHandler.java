package com.github.dataflow.core.instance.handler;

/**
 * 消息中间件实例处理器
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/5
 */
public abstract class AbstractMessageAwareInstanceHandler extends AbstractInstanceHandler {
    /**
     * 消费获取时延
     */
    protected final long DEFAULT_TIMEOUT = 2000;

    /**
     * 消费获取时间间隔
     */
    protected final long DEFAULT_PERIOD  = 100;
}
