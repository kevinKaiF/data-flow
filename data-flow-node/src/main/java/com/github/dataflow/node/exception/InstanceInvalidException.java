package com.github.dataflow.node.exception;

/**
 * @author kevin
 * @date 2017-05-31 10:12 PM.
 */
public class InstanceInvalidException extends RuntimeException {
    public InstanceInvalidException() {
        super();
    }

    public InstanceInvalidException(String message) {
        super(message);
    }

    public InstanceInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceInvalidException(Throwable cause) {
        super(cause);
    }

    protected InstanceInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
