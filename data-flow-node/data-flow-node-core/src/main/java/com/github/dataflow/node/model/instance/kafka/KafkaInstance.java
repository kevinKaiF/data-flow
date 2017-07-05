package com.github.dataflow.node.model.instance.kafka;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.core.instance.AbstractMessageAwareInstance;
import com.github.dataflow.core.instance.config.MessageAwareInstanceConfig;
import com.github.dataflow.sender.kafka.config.KafkaConfig;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/30
 */
public class KafkaInstance extends AbstractMessageAwareInstance {
    private        Logger     logger     = LoggerFactory.getLogger(KafkaInstance.class);
    private static AtomicLong atomicLong = new AtomicLong(0);
    private Consumer<String, String> consumer;
    private String                   topic;
    private long                     timeout;
    private long                     period;

    private KafkaInstance() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public KafkaInstance(Properties options) {
        this.options = options;
        this.topic = PropertyUtil.getString(options, KafkaConfig.TOPIC);
        this.timeout = PropertyUtil.getLong(options, MessageAwareInstanceConfig.POLL_TIMEOUT);
        this.period = PropertyUtil.getLong(options, MessageAwareInstanceConfig.POLL_PERIOD);
    }

    protected void initReceiveThread() {
        logger.info("init receive thread begin...");
        super.initReceiveThread();
        logger.info("init receive thread end!");
    }

    protected String getThreadName() {
        return "kafkaInstance-" + atomicLong.getAndIncrement();
    }

    protected void initConsumer() {
        logger.info("init consumer begin...");
        consumer = new KafkaConsumer<>(options);
        consumer.subscribe(toList(topic));
        logger.info("init consumer end!");
    }

    public void doStart() {
        logger.info("start KafkaInstance for {} / {} with parameters:{}", new Object[]{this.id, this.name, this.options});
        super.doStart();
        logger.info("start KafkaInstance successfully.");
    }

    public void doStop() {
        logger.info("stop KafkaInstance for {} / {} ", new Object[]{this.id, this.name});
        super.doStop();
        logger.info("stop KafkaInstance successfully.");
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

    @Override
    protected ReceiveTask newReceiveTask() {
        return new ReceiveTask() {
            @Override
            protected void closeConsumer() {
                consumer.close();
                semaphore.release();
                logger.info("close kafka consumer successfully.");
            }

            @Override
            protected void handle(List<RowMetaData> rowMetaDataList) throws Exception {
                dataStore.handle(rowMetaDataList);
            }

            @Override
            public void run() {
                Throwable ex = null;
                while (running && ex == null) {
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

                        Thread.sleep(period);
                    } catch (InterruptedException e) {
                        logger.info("ReceiveTask accept interruption successfully.");
                        ex = e;
                    } catch (Throwable e) {
                        logger.error("ReceiveTask happened exception, detail : ", e);
                        String fullStackTrace = ExceptionUtils.getFullStackTrace(e);
                        alarmService.sendAlarm(name, fullStackTrace);
                        ex = e;
                    } finally {
                        if (!running) {
                            closeConsumer();
                            doStop();
                        } else if (ex != null) {
                            closeConsumer();
                            stop();
                        } else {
                            // do nothing
                        }
                    }
                }
            }
        };
    }
}
