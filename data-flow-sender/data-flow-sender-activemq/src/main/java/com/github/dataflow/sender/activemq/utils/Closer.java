package com.github.dataflow.sender.activemq.utils;

import javax.jms.*;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/5
 */
public class Closer {
    public static void closeSessionQuietly(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeConnectionQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                connection.stop();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeProducerQuietly(MessageProducer producer) {
        if (producer != null) {
            try {
                producer.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeConsumerQuietly(MessageConsumer consumer) {
        if (consumer != null) {
            try {
                consumer.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
