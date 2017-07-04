package com.github.dataflow.sender.activemq.config;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
public class ActivemqConfig {

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    /**
     * 消息发送类型，QUEUE or TOPIC
     */
    public static final String TYPE = "type";

    /**
     * 队列名称
     */
    public static final String QUEUE = "queue";

    /**
     * 主题名称
     */
    public static final String TOPIC = "topic";

    /**
     * producer传输模式
     */
    public static final String DELIVERY_MODE = "deliveryMode";

    public static final String BROKE_URL = "brokeUrl";
}
