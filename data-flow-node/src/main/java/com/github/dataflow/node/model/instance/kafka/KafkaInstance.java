package com.github.dataflow.node.model.instance.kafka;

import com.alibaba.fastjson.JSON;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.core.exception.InstanceException;
import com.github.dataflow.core.instance.AbstractInstance;
import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/30
 */
public class KafkaInstance extends AbstractInstance {
    private       Logger    logger              = LoggerFactory.getLogger(KafkaInstance.class);
    private final String    DEFAULT_TIMEOUT_STR = "2000";
    private final String    DEFAULT_PERIOD_STR  = "100";
    private final String    PROP_TOPIC          = "kafka.topic";
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private       Semaphore semaphore           = new Semaphore(0);

    private Consumer<String, String> consumer;
    private String                   topic;
    private long                     timeout;
    private long                     period;
    private Thread                   receiveThread;
    private Properties options;

    private Properties getProperties() {
        Properties prop = options;
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
            throw new InstanceException("no configure the property [" + property + "]");
        }
    }

    @Override
    protected void doInit() {
        // init kafka
        consumer = new KafkaConsumer<>(getProperties());
        consumer.subscribe(toList(topic));
        // poll mode
        receiveThread = new Thread(new ReceiveTask(), "kafkaInstance-" + atomicInteger.getAndIncrement());
        receiveThread.setDaemon(true);
        receiveThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {

            }
        });
    }

    public void start() {
        super.start();
        logger.info("start KafkaInstance successfully.");
        receiveThread.start();
    }

    public void stop() {
        super.stop();
        try {
            semaphore.acquire(1);
            logger.info("stop KafkaInstance successfully.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private List<String> toList(String topic) {
        return Arrays.asList(topic.split(","));
    }

    @Override
    public String getPosition(String instanceName) {
        return null;
    }

    public void setOptions(Properties options) {
        this.options = options;
    }

    public Properties getOptions() {
        return options;
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
                        handle(parseRowMetaData(value));
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
                    throw new InstanceException(e);
                }
            }

            closeConsumer();
        }

        private void closeConsumer() {
            consumer.close();
            semaphore.release();
            logger.info("close kafka consumer successfully.");
        }

        private void handle(List<RowMetaData> rowMetaDataList) throws Exception {
            dataStore.handle(rowMetaDataList);
        }

        private List<RowMetaData> parseRowMetaData(String value) {
            return JSON.parseArray(value, RowMetaData.class);
        }
    }
}
