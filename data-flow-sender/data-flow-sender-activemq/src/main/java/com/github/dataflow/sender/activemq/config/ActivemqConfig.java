package com.github.dataflow.sender.activemq.config;

import com.github.dataflow.sender.core.config.MessageQueueMappingConfig;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
public interface ActivemqConfig {
    interface MappingConfig extends MessageQueueMappingConfig {
        String USERNAME = "username";

        String PASSWORD = "password";

        /**
         * 消息发送类型，QUEUE or TOPIC
         */
        String TYPE = "type";

        /**
         * 队列名称
         */
        String QUEUE = "queue";

        /**
         * 主题名称
         */
        String TOPIC = "topic";

        /**
         * producer传输模式
         */
        String DELIVERY_MODE = "deliveryMode";
    }

    String BROKE_URL = "brokeUrl";
}
