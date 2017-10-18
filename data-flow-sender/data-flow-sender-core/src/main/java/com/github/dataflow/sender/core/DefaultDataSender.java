package com.github.dataflow.sender.core;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/10/18
 */
public class DefaultDataSender extends DataSender {
    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    protected void doSend(Object transformedData) throws Exception {
        // no-op
    }

    @Override
    protected void doStart() {
        // no-op
    }

    @Override
    protected void doStop() {
        // no-op
    }
}
