package com.github.dataflow.sender.kafka.handler;

import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.AbstractDataSenderHandler;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.sender.kafka.KafkaDataSender;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author kevin
 * @date 2017-05-30 1:02 AM.
 */
public class KafkaDataSenderHandler extends AbstractDataSenderHandler {
    private DataSourceType dataSourceType = DataSourceType.KAFKA;

    public boolean support(int type) {
        return dataSourceType.getType() == type;
    }

    public DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        String topic = dataOutputMapping.getTopic();
        if (StringUtils.isEmpty(topic)) {
            throw new DataSenderException("the topic property of DataOutputMapping must not be null.");
        }

        Properties props = parseToProperties(dataOutputMapping.getDataSourceOutput().getOptions());
        String servers = props.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);
        if (StringUtils.isEmpty(servers)) {
            throw new DataSenderException("the bootstrap.servers property of DataSourceOutput must not be null.");
        }

        props.put("topic", topic);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        KafkaDataSender kafkaDataSender = new KafkaDataSender(props);
        return kafkaDataSender;
    }


}
