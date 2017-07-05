package com.github.dataflow.sender.activemq;

import com.alibaba.fastjson.JSON;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.sender.activemq.config.ActivemqConfig;
import com.github.dataflow.sender.activemq.enums.ActivemqType;
import com.github.dataflow.sender.activemq.utils.Closer;
import com.github.dataflow.sender.core.DataSender;
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
    private Properties      options;
    private Session         session;
    private Connection      connection;
    private MessageProducer producer;

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
            Destination destination = null;
            if (PropertyUtil.getInt(options, ActivemqConfig.TYPE) == ActivemqType.QUEUE.getType()) {
                destination = session.createQueue(PropertyUtil.getString(options, ActivemqConfig.QUEUE));
            } else {
                destination = session.createTopic(PropertyUtil.getString(options, ActivemqConfig.TOPIC));
            }
            producer = session.createProducer(destination);
            producer.setDeliveryMode(PropertyUtil.getInt(options, ActivemqConfig.DELIVERY_MODE));
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
        init();
    }

    @Override
    protected void doStop() {
        Closer.closeProducerQuietly(producer);
        Closer.closeSessionQuietly(session);
        Closer.closeConnectionQuietly(connection);
    }
}
