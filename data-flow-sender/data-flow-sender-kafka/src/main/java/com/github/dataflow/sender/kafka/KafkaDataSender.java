package com.github.dataflow.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.sender.core.DataSender;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;
import java.util.Properties;

/**
 * @author kevin
 * @date 2017-05-29 10:59 PM.
 */
public class KafkaDataSender extends DataSender {
    private Producer<String, String> producer;
    private String                   topic;

    public KafkaDataSender(Properties props) {
        producer = new KafkaProducer<>(props);
        topic = props.getProperty("topic");
    }

    public void send(List<RowMetaData> rowMetaDataList) {
        producer.send(new ProducerRecord<String, String>(topic, JSON.toJSONString(rowMetaDataList)));
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
