package com.github.dataflow.sender.activemq;

import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.sender.activemq.config.ActivemqConfig;
import com.github.dataflow.sender.activemq.enums.ActivemqType;
import com.github.dataflow.sender.activemq.utils.Closer;
import com.github.dataflow.sender.core.TransformedDataSender;
import com.github.dataflow.sender.core.exception.DataSenderException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
public class ActivemqDataSender extends TransformedDataSender {
    private Logger                logger    = LoggerFactory.getLogger(ActivemqDataSender.class);
    private List<MessageProducer> producers = new ArrayList<>();
    private Properties options;
    private Session    session;
    private Connection connection;

    private ActivemqDataSender() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public ActivemqDataSender(Properties properties) {
        this.options = properties;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    private void init() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    PropertyUtil.getString(options, ActivemqConfig.USERNAME),
                    PropertyUtil.getString(options, ActivemqConfig.PASSWORD),
                    PropertyUtil.getString(options, ActivemqConfig.BROKE_URL));
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            int deliveryMode = PropertyUtil.getInt(options, ActivemqConfig.DELIVERY_MODE);
            if (PropertyUtil.getInt(options, ActivemqConfig.TYPE) == ActivemqType.QUEUE.getType()) {
                String queues = PropertyUtil.getString(options, ActivemqConfig.QUEUE);
                String[] queueArr = queues.split(",");
                int length = queueArr.length;
                for (int i = 0; i < length; i++) {
                    Queue queue = session.createQueue(queueArr[i]);
                    MessageProducer producer = session.createProducer(queue);
                    producer.setDeliveryMode(deliveryMode);
                    producers.add(producer);
                }
            } else {
                String topics = PropertyUtil.getString(options, ActivemqConfig.TOPIC);
                String[] topicArr = topics.split(",");
                int length = topicArr.length;
                for (int i = 0; i < length; i++) {
                    Topic topic = session.createTopic(topicArr[i]);
                    MessageProducer producer = session.createProducer(topic);
                    producer.setDeliveryMode(deliveryMode);
                    producers.add(producer);
                }
            }
        } catch (JMSException e) {
            throw new DataSenderException(e);
        }
    }

    @Override
    protected void doSend(String transformedValue) throws Exception {
        TextMessage message = session.createTextMessage(transformedValue);
        for (MessageProducer producer : producers) {
            producer.send(message);
        }
        session.commit();
    }

    @Override
    protected void doStart() {
        logger.info("start to init ActivemqDataSender [{}:{}], params : {}", dataSenderId, dataSenderName, options);
        init();
        logger.info("start to init ActivemqDataSender successfully.");
    }

    @Override
    protected void doStop() {
        logger.info("start to stop ActivemqDataSender [{}:{}], params : {}", dataSenderId, dataSenderName, options);
        int size = producers.size();
        for (int i = 0; i < size; i++) {
            try {
                Closer.closeProducer(producers.get(i));
            } catch (JMSException e) {
                logger.error("close activemq producer [{}] failure, index = {}, Producers = {}, detail : ", producers.get(i), i, producers, e);
            }
        }

        try {
            Closer.closeSession(session);
        } catch (JMSException e) {
            logger.error("close activemq session failure, detail : ", e);
        }

        try {
            Closer.closeConnection(connection);
        } catch (JMSException e) {
            logger.error("close activemq connection failure, detail : ", e);
        }

        logger.info("start to stop ActivemqDataSender successfully.");
    }
}
