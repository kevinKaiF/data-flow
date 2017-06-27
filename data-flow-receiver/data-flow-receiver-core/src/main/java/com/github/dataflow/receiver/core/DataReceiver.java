package com.github.dataflow.receiver.core;

import com.github.dataflow.common.model.DataFlowLifeCycle;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/24
 */
public interface DataReceiver extends DataFlowLifeCycle {
    @Override
    void start();

    @Override
    void stop();

    @Override
    boolean isStart();
}
