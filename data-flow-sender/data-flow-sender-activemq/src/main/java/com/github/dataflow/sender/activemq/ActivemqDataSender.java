package com.github.dataflow.sender.activemq;

import com.alibaba.fastjson.JSON;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.sender.activemq.config.ActivemqConfig;
import com.github.dataflow.sender.activemq.enums.ActivemqType;
import com.github.dataflow.sender.core.DataSender;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;
import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
public class ActivemqDataSender extends DataSender {
    private Session         session;
    private Connection      connection;
    private MessageProducer producer;

    private ActivemqDataSender() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public ActivemqDataSender(Properties properties) {
        init(properties);
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    private void init(Properties properties) {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    PropertyUtil.getString(properties, ActivemqConfig.USERNAME, ActiveMQConnection.DEFAULT_USER),
                    PropertyUtil.getString(properties, ActivemqConfig.PASSWORD, ActiveMQConnection.DEFAULT_PASSWORD),
                    PropertyUtil.getString(properties, ActivemqConfig.BROKE_URL));
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = null;
            if (PropertyUtil.getInt(properties, ActivemqConfig.TYPE) == ActivemqType.QUEUE.getType()) {
                destination = session.createQueue(PropertyUtil.getString(properties, ActivemqConfig.QUEUE));
            } else {
                destination = session.createTopic(PropertyUtil.getString(properties, ActivemqConfig.TOPIC));
            }
            producer = session.createProducer(destination);
            producer.setDeliveryMode(PropertyUtil.getInt(properties, ActivemqConfig.DELIVERY_MODE, DeliveryMode.NON_PERSISTENT));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(List<RowMetaData> rowMetaDataList) throws Exception {
        TextMessage message = session.createTextMessage(JSON.toJSONString(rowMetaDataList));
        producer.send(message);
        session.commit();
    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {
        try {
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            try {
                connection.close();
                connection.stop();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
