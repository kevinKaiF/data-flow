package com.github.dataflow.receiver;

import com.github.dataflow.receiver.kafka.KafkaDataReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/15
 */
public class KafkaReceiverLauncher {
    private static final Logger logger = LoggerFactory.getLogger(KafkaReceiverLauncher.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = null;
        try {
            context = new ClassPathXmlApplicationContext("classpath:application-context.xml");
            final KafkaDataReceiver kafkaDataReceiver = (KafkaDataReceiver) context.getBean("kafkaDataReceiver");
            addShutdownHook(kafkaDataReceiver, context);
            kafkaDataReceiver.start();
        } catch (Exception e) {
            logger.error("kafka receiver stop on exception, detail : ", e);
        }
    }

    private static void addShutdownHook(final KafkaDataReceiver kafkaDataReceiver, final ClassPathXmlApplicationContext context) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                kafkaDataReceiver.stop();
                context.close();
            }
        }));
    }
}
