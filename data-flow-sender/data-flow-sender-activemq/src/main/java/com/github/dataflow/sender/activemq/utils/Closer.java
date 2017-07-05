package com.github.dataflow.sender.activemq.utils;

import javax.jms.*;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/5
 */
public class Closer {
    public static void closeSession(Session session) throws JMSException {
        if (session != null) {
            session.close();
        }
    }

    public static void closeConnection(Connection connection) throws JMSException {
        if (connection != null) {
            connection.stop();
            connection.close();
        }
    }

    public static void closeProducer(MessageProducer producer) throws JMSException {
        if (producer != null) {
            producer.close();
        }
    }

    public static void closeConsumer(MessageConsumer consumer) throws JMSException {
        if (consumer != null) {
            consumer.close();
        }
    }
}
