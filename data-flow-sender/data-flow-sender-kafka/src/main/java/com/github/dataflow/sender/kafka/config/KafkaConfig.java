package com.github.dataflow.sender.kafka.config;

import com.github.dataflow.sender.core.config.MessageQueueMappingConfig;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
public interface KafkaConfig {
    interface MappingConfig extends MessageQueueMappingConfig {
        String TOPIC = "topic";

        // 指定消费端的分区
        String TOPIC_PARTITION = "topicPartition";
    }
}
