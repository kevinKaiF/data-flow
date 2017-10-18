package com.github.dataflow.sender.activemq;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.sender.activemq.config.ActivemqConfig;
import com.github.dataflow.sender.activemq.enums.ActivemqType;
import com.github.dataflow.sender.activemq.enums.MessageType;
import com.github.dataflow.sender.activemq.utils.Closer;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.config.MessageQueueMappingConfig;
import com.github.dataflow.sender.core.exception.DataSenderException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
public class ActivemqDataSender extends DataSender {
    private Logger                logger    = LoggerFactory.getLogger(ActivemqDataSender.class);
    private List<MessageProducer> producers = new ArrayList<>();
    private JSONObject options;
    private Session    session;
    private Connection connection;

    private ActivemqDataSender() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public ActivemqDataSender(JSONObject properties) {
        this.options = properties;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    private void init() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    JSONObjectUtil.getString(options, ActivemqConfig.MappingConfig.USERNAME),
                    JSONObjectUtil.getString(options, ActivemqConfig.MappingConfig.PASSWORD),
                    JSONObjectUtil.getString(options, ActivemqConfig.BROKE_URL));
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            int deliveryMode = JSONObjectUtil.getInt(options, ActivemqConfig.MappingConfig.DELIVERY_MODE);
            if (JSONObjectUtil.getInt(options, ActivemqConfig.MappingConfig.TYPE) == ActivemqType.QUEUE.getType()) {
                String queues = JSONObjectUtil.getString(options, ActivemqConfig.MappingConfig.QUEUE);
                String[] queueArr = queues.split(",");
                int length = queueArr.length;
                for (int i = 0; i < length; i++) {
                    Queue queue = session.createQueue(queueArr[i]);
                    MessageProducer producer = session.createProducer(queue);
                    producer.setDeliveryMode(deliveryMode);
                    producers.add(producer);
                }
            } else {
                String topics = JSONObjectUtil.getString(options, ActivemqConfig.MappingConfig.TOPIC);
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
    protected void doSend(Object transformedValue) throws Exception {
        String messageType = JSONObjectUtil.getString(options, MessageQueueMappingConfig.MESSAGE_TYPE);
        List<Object> messageList = null;
        if (transformedValue instanceof List) {
            messageList = ((List) transformedValue);
        } else {
            messageList = Collections.singletonList(transformedValue);
        }

        for (Object messageEntity : messageList) {
            Message message;
            if (MessageType.OBJECT.getType().equalsIgnoreCase(messageType)) {
                message = session.createObjectMessage((Serializable) messageEntity);
            } else {
                message = session.createTextMessage((String) messageEntity);
            }
            for (MessageProducer producer : producers) {
                producer.send(message);
            }
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
