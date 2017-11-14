package com.github.dataflow.node.exception;

/**
 * @author kevin
 * @date 2017-06-01 1:37 AM.
 */
public class DataStoreHandleException extends RuntimeException {
    public DataStoreHandleException() {
        super();
    }

    public DataStoreHandleException(String message) {
        super(message);
    }

    public DataStoreHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataStoreHandleException(Throwable cause) {
        super(cause);
    }

    protected DataStoreHandleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
