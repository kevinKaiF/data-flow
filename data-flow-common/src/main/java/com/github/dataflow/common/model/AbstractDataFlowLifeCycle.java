package com.github.dataflow.common.model;

import com.github.dataflow.common.exception.DataFlowLifeCycleException;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/1
 */
public class AbstractDataFlowLifeCycle {
    protected volatile boolean running = false;

    public AbstractDataFlowLifeCycle() {
    }

    public boolean isStart() {
        return this.running;
    }

    public void start() {
        ensureIsStop();
        this.running = true;
    }

    private void ensureIsStop() {
        if (this.running) {
            throw new DataFlowLifeCycleException(this.getClass().getName() + " has startup , don't repeat start.");
        }
    }

    public void stop() {
        ensureIsStart();
        this.running = false;
    }

    private void ensureIsStart() {
        if (!this.running) {
            throw new DataFlowLifeCycleException(this.getClass().getName() + " isn\'t start , please check.");
        }
    }
}
