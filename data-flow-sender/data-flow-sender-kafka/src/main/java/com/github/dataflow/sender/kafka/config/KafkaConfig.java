package com.github.dataflow.sender.kafka.config;

import com.github.dataflow.sender.core.config.MessageQueueMappingConfig;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
public class KafkaConfig {
    public static class MappingConfig extends MessageQueueMappingConfig {
        public static final String TOPIC = "topic";

        // 指定消费端的分区
        public static final String TOPIC_PARTITION = "topicPartition";
    }
}
