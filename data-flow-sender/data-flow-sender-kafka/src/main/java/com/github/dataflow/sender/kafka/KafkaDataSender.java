package com.github.dataflow.sender.kafka;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.sender.core.TransformedDataSender;
import com.github.dataflow.sender.kafka.config.KafkaConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author kevin
 * @date 2017-05-29 10:59 PM.
 */
public class KafkaDataSender extends TransformedDataSender {
    private Producer<String, String> producer;
    private String                   topic;

    @Override
    public boolean isSingleton() {
        return false;
    }

    public KafkaDataSender(JSONObject props) {
        producer = new KafkaProducer<>(props);
        topic = props.getString(KafkaConfig.TOPIC);
    }

    @Override
    protected void doSend(String transformedValue) throws Exception {
        producer.send(new ProducerRecord<String, String>(topic, transformedValue));
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
