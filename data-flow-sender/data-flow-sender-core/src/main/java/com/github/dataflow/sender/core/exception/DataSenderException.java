package com.github.dataflow.sender.core.exception;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/1
 */
public class DataSenderException extends RuntimeException {
    public DataSenderException() {
        super();
    }

    public DataSenderException(String message) {
        super(message);
    }

    public DataSenderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSenderException(Throwable cause) {
        super(cause);
    }

    protected DataSenderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
