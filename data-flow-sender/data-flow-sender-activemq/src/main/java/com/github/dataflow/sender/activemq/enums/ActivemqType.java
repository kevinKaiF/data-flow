package com.github.dataflow.sender.activemq.enums;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
public enum ActivemqType {
    QUEUE(0),
    TOPIC(1);

    private int type;

    ActivemqType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
