package com.github.dataflow.common.model;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/1
 */
public interface DataFlowLifeCycle {
    void start();

    void stop();

    boolean isStart();
}
