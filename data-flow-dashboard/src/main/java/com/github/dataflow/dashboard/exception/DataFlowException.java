package com.github.dataflow.dashboard.exception;

/**
 * @author : version
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/10
 */
public class DataFlowException extends RuntimeException {
    public DataFlowException() {
        super();
    }

    public DataFlowException(String message) {
        super(message);
    }

    public DataFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataFlowException(Throwable cause) {
        super(cause);
    }

    protected DataFlowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
