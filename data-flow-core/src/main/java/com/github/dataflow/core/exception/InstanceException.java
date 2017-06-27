package com.github.dataflow.core.exception;

/**
 * @author kevin
 * @date 2017-05-30 12:33 AM.
 */
public class InstanceException extends RuntimeException {
    public InstanceException() {
        super();
    }

    public InstanceException(String message) {
        super(message);
    }

    public InstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceException(Throwable cause) {
        super(cause);
    }
}
