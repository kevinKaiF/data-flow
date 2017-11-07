package com.github.dataflow.node.model.instance.activemq;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.core.exception.InstanceException;
import com.github.dataflow.core.instance.AbstractMessageAwareInstance;
import com.github.dataflow.sender.activemq.config.ActivemqConfig;
import com.github.dataflow.sender.activemq.enums.ActivemqType;
import com.github.dataflow.sender.activemq.utils.Closer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
public class ActivemqInstance extends AbstractMessageAwareInstance {
    private Logger logger = LoggerFactory.getLogger(ActivemqInstance.class);

    private static final AtomicLong atomicLong = new AtomicLong(0);

    private Connection connection;

    private Session session;

    private MessageConsumer consumer;

    private ActivemqInstance() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public ActivemqInstance(String instanceName, JSONObject options) {
        super(instanceName);
        this.options = options;
    }

    @Override
    protected void doStart() {
        logger.info("start ActiveMqInstance for {} / {} with parameters:{}", new Object[]{this.id, this.name, this.options});
        super.doStart();
        logger.info("start ActiveMqInstance successfully.");
    }

    @Override
    protected void doStop() {
        logger.info("stop ActiveMqInstance for {} / {} ", new Object[]{this.id, this.name});
        super.doStop();
        logger.info("stop ActiveMqInstance successfully.");
    }

    @Override
    public String getPosition(String instanceName) {
        return null;
    }

    protected void initReceiveThread() {
        logger.info("init receive thread begin...");
        super.initReceiveThread();
        logger.info("init receive thread end!");
    }

    protected void initConsumer() {
        try {
            logger.info("init consumer begin...");
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    JSONObjectUtil.getString(options, ActivemqConfig.MappingConfig.USERNAME),
                    JSONObjectUtil.getString(options, ActivemqConfig.MappingConfig.PASSWORD),
                    JSONObjectUtil.getString(options, ActivemqConfig.BROKE_URL));
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = null;
            if (JSONObjectUtil.getInt(options, ActivemqConfig.MappingConfig.TYPE) == ActivemqType.QUEUE.getType()) {
                destination = session.createQueue(JSONObjectUtil.getString(options, ActivemqConfig.MappingConfig.QUEUE));
            } else {
                destination = session.createTopic(JSONObjectUtil.getString(options, ActivemqConfig.MappingConfig.TOPIC));
            }

            consumer = session.createConsumer(destination);
            logger.info("init consumer end!");
        } catch (JMSException e) {
            throw new InstanceException(e);
        }
    }

    @Override
    protected String getThreadName() {
        return "ActiveMqInstance-" + instanceName + "-" + atomicLong.getAndIncrement();
    }

    @Override
    protected ReceiveTask newReceiveTask() {
        return new ReceiveTask() {
            @Override
            protected void closeConsumer() {
                try {
                    Closer.closeConsumer(consumer);
                    logger.info("close activeMq consumer successfully.");
                } catch (JMSException e) {
                    logger.error("close activeMq consumer failure, detail : ", e);
                } finally {
                    semaphore.release();
                }
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
                        TextMessage message = (TextMessage) consumer.receive(timeout);
                        String value = message.getText();
                        logger.debug("ReceiveTask receive data : " + value);
                        List<RowMetaData> rowMetaDataList = parseRowMetaData(value);
                        do {
                            try {
                                handle(rowMetaDataList);
                            } catch (Throwable e) {
                                handleException(e);
                                ex = e;
                            } finally {
                                Thread.sleep(period);
                            }
                        } while (running && ex != null && !(ex instanceof InterruptedException));
                        Thread.sleep(period);
                    } catch (Throwable e) {
                        handleException(e);
                        ex = e;
                    } finally {
                        if (!running || ex instanceof InterruptedException) {
                            closeConsumer();
                            doStop();
                        }
                    }
                }
            }

            private void handleException(Throwable e) {
                if (e instanceof InterruptedException) {
                    logger.info("ReceiveTask accept interruption successfully.");
                } else {
                    logger.error("ReceiveTask happened exception, detail : ", e);
                    String fullStackTrace = ExceptionUtils.getFullStackTrace(e);
                    alarmService.sendAlarm(name, fullStackTrace);
                }
            }
        };
    }
}
