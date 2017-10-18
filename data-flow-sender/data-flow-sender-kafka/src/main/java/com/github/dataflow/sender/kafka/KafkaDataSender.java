package com.github.dataflow.sender.kafka;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.kafka.config.KafkaConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author kevin
 * @date 2017-05-29 10:59 PM.
 */
public class KafkaDataSender extends DataSender {
    private Producer<String, Object> producer;
    private String                   topic;

    @Override
    public boolean isSingleton() {
        return false;
    }

    public KafkaDataSender(JSONObject props) {
        producer = new KafkaProducer<>(props);
        topic = props.getString(KafkaConfig.MappingConfig.TOPIC);
    }

    @Override
    protected void doSend(Object transformedValue) throws Exception {
        producer.send(new ProducerRecord<String, Object>(topic, transformedValue));
    }

    @Override
    protected void doStart() {
    }

    @Override
    protected void doStop() {
        producer.flush();
        producer.close();
    }
}
