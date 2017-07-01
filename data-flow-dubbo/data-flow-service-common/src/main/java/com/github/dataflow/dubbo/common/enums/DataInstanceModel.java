package com.github.dataflow.dubbo.common.enums;

/**
 * 实例的模式，生产者/消费者
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/30
 */
public enum DataInstanceModel {
    PRODUCER(0),
    CONSUMER(1);

    private int type;

    DataInstanceModel(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
