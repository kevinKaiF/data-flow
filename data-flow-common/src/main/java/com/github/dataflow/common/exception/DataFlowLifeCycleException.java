package com.github.dataflow.common.exception;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/24
 */
public class DataFlowLifeCycleException extends RuntimeException {
    public DataFlowLifeCycleException() {
        super();
    }

    public DataFlowLifeCycleException(String message) {
        super(message);
    }

    public DataFlowLifeCycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataFlowLifeCycleException(Throwable cause) {
        super(cause);
    }

    protected DataFlowLifeCycleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
