package com.github.dataflow.sender.activemq.enums;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/10/17
 */
public enum MessageType {
    OBJECT("object"),
    TEXT("text");

    private String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
