//package com.github.dataflow.node.model.sender.handler;
//
//import com.alibaba.fastjson.JSONObject;
//import com.github.dataflow.core.exception.DataSenderException;
//import com.github.dataflow.core.sender.AbstractDataSenderHandler;
//import com.github.dataflow.core.sender.DataSender;
//import com.github.dataflow.dubbo.model.DataOutputMapping;
//import com.github.dataflow.dubbo.common.enums.DataSourceOutputType;
//import com.github.dataflow.node.model.sender.KafkaDataSender;
//import org.apache.commons.lang.StringUtils;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.springframework.stereotype.Component;
//
//import java.util.Properties;
//
///**
// * @author kevin
// * @date 2017-05-30 1:02 AM.
// */
//@Component
//public class KafkaDataSenderHandler extends AbstractDataSenderHandler {
//    private DataSourceOutputType dataSenderType = DataSourceOutputType.KAFKA;
//
//    public boolean support(int type) {
//        return dataSenderType.getType() == type;
//    }
//
//    public DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
//        String topic = dataOutputMapping.getTopic();
//        if (StringUtils.isBlank(topic)) {
//            throw new DataSenderException("the topic property of DataOutputMapping must not be null.");
//        }
//
//        Properties props = parseToProperties(dataOutputMapping.getDataSourceOutput().getOptions());
//        String servers = props.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);
//        if (StringUtils.isBlank(servers)) {
//            throw new DataSenderException("the bootstrap.servers property of DataSourceOutput must not be null.");
//        }
//
//        props.put("topic", topic);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        KafkaDataSender kafkaDataSender = new KafkaDataSender(props);
//        return kafkaDataSender;
//    }
//
//    private Properties parseToProperties(String options) {
//        return JSONObject.parseObject(options, Properties.class);
//    }
//}
