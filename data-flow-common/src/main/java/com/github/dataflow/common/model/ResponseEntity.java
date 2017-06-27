package com.github.dataflow.common.model;

/**
 * 请求响应实体
 *
 * @author kevin
 * @date 2017-05-28 2:50 PM.
 */
public class ResponseEntity {
    public static final int SUCCESS = 200;
    public static final int FAILURE = 500;

    private int responseStatus;

    private String message;

    private Object result;

    public ResponseEntity() {
        this.responseStatus = SUCCESS;
    }

    public ResponseEntity(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void clone(ResponseEntity responseEntity) {
        this.setResponseStatus(responseEntity.getResponseStatus());
        this.setResult(responseEntity.getResult());
        this.setMessage(responseEntity.getMessage());
    }
}
