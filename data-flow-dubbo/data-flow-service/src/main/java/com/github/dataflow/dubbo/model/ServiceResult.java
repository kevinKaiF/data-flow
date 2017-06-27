package com.github.dataflow.dubbo.model;

import java.io.Serializable;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/1
 */
public class ServiceResult<T> implements Serializable {
    private static final long    serialVersionUID = 1513617048439900895L;
    private              boolean success          = true;

    private String errorMessage;

    private T result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
