package com.github.dataflow.transformer.exception;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/21
 */
public class DataTransformerException extends RuntimeException {
    public DataTransformerException() {
        super();
    }

    public DataTransformerException(String message) {
        super(message);
    }

    public DataTransformerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataTransformerException(Throwable cause) {
        super(cause);
    }

    protected DataTransformerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
