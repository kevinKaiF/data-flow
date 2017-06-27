package com.github.dataflow.receiver.kafka;

import com.alibaba.fastjson.JSON;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.receiver.core.AbstractDataReceiver;
import com.github.dataflow.receiver.core.exception.ReceiverException;
import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Semaphore;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/15
 */
@Component
public class KafkaDataReceiver extends AbstractDataReceiver implements InitializingBean {
    private       Logger    logger              = LoggerFactory.getLogger(KafkaDataReceiver.class);
    private final String    DEFAULT_TIMEOUT_STR = "2000";
    private final String    DEFAULT_PERIOD_STR  = "100";
    private final String    PROP_TOPIC          = "kafka.topic";
    private       Semaphore semaphore           = new Semaphore(0);

    private Consumer<String, String> consumer;
    private String                   topic;
    private long                     timeout;
    private long                     period;
    private ReceiveTask              receiveTask;

    private Properties getProperties() throws IOException {
        validateProperties(prop, ConsumerConfig.GROUP_ID_CONFIG);
        validateProperties(prop, ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG);
        validateProperties(prop, PROP_TOPIC);
        topic = prop.getProperty(PROP_TOPIC);
        timeout = Long.valueOf(prop.getProperty("kafka.poll.timeout", DEFAULT_TIMEOUT_STR));
        period = Long.valueOf(prop.getProperty("kafka.poll.period", DEFAULT_PERIOD_STR));
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        return prop;
    }

    private void validateProperties(Properties prop, String property) {
        if (StringUtils.isEmpty(prop.getProperty(property))) {
            throw new ReceiverException("no configure the property [" + property + "]");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // init kafka
        consumer = new KafkaConsumer<>(getProperties());
        consumer.subscribe(toList(topic));
        // poll mode
        receiveTask = new ReceiveTask();
    }

    public void start() {
        super.start();
        logger.info("start KafkaDataReceiver successfully.");
        receiveTask.run();
    }

    public void stop() {
        super.stop();
        try {
            semaphore.acquire(1);
            logger.info("stop KafkaDataReceiver successfully.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private List<String> toList(String topic) {
        return Arrays.asList(topic.split(","));
    }

    private class ReceiveTask implements Runnable {
        @Override
        public void run() {
            while (running) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(timeout);
                    Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
                    while (iterator.hasNext() && running) {
                        ConsumerRecord<String, String> next = iterator.next();
                        String value = next.value();
                        logger.debug("ReceiveTask receive data : " + value);
                        send(parseRowMetaData(value));
                        consumer.commitSync();
                    }

                    try {
                        Thread.sleep(period);
                    } catch (InterruptedException e) {
                        logger.info("ReceiveTask accept interruption successfully.");
                        running = false;
                    }
                } catch (Exception e) {
                    closeConsumer();
                    throw new ReceiverException(e);
                }
            }

            closeConsumer();
            semaphore.release();
        }

        private void closeConsumer() {
            consumer.close();
            logger.info("close kafka consumer successfully.");
        }

        private void send(List<RowMetaData> rowMetaDataList) throws Exception {
            dataSender.send(rowMetaDataList);
        }

        private List<RowMetaData> parseRowMetaData(String value) {
            return JSON.parseArray(value, RowMetaData.class);
        }
    }

}
