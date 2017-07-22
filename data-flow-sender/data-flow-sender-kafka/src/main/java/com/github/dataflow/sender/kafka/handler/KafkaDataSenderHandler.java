package com.github.dataflow.sender.kafka.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.sender.core.handler.TransformedDataSenderHandler;
import com.github.dataflow.sender.kafka.KafkaDataSender;
import com.github.dataflow.sender.kafka.config.KafkaConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.util.StringUtils;

/**
 * @author kevin
 * @date 2017-05-30 1:02 AM.
 */
public class KafkaDataSenderHandler extends TransformedDataSenderHandler {
    private DataSourceType dataSourceType = DataSourceType.KAFKA;

    public boolean support(int type) {
        return dataSourceType.getType() == type;
    }

    public DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {

        JSONObject props = parseJSON(dataOutputMapping.getDataSourceOutput().getOptions());
        String servers = props.getString(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);
        if (StringUtils.isEmpty(servers)) {
            throw new DataSenderException("the bootstrap.servers property of DataSourceOutput must not be null.");
        }
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        JSONObject dataOutputMappingOptions = refreshDataOutputMapping(dataOutputMapping);
        props.putAll(dataOutputMappingOptions);
        return new KafkaDataSender(props);
    }

    @Override
    protected JSONObject refreshDataOutputMapping(DataOutputMapping dataOutputMapping) {
        JSONObject dataOutputMappingOptions = parseJSON(dataOutputMapping.getOptions());
        String topic = dataOutputMappingOptions.getString(KafkaConfig.TOPIC);
        if (StringUtils.isEmpty(topic)) {
            throw new DataSenderException("the topic property of DataOutputMapping must not be null.");
        }

        return dataOutputMappingOptions;
    }
}
