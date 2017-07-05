package com.github.dataflow.core.instance;

import com.alibaba.fastjson.JSON;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.core.instance.config.MessageAwareInstanceConfig;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Semaphore;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/5
 */
public abstract class AbstractMessageAwareInstance extends AbstractInstance {
    protected Semaphore semaphore = new Semaphore(0);

    protected Thread receiveThread;

    protected Properties options;

    @Override
    protected void doInit() {
        initConsumer();
        initReceiveThread();
    }

    @Override
    protected void doStart() {
        receiveThread.start();

        if (!dataStore.isStart()) {
            dataStore.start();
        }

        if (!alarmService.isStart()) {
            alarmService.start();
        }
    }

    @Override
    protected void doStop() {
        try {
            // wait util receiveThread die
            semaphore.acquire(1);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        if (dataStore.isStart()) {
            dataStore.stop();
        }
    }

    /**
     * 初始化consumer
     */
    protected abstract void initConsumer();

    /**
     * 初始化消费线程
     */
    protected void initReceiveThread() {
        receiveThread = new Thread(newReceiveTask(), getThreadName());
        receiveThread.setDaemon(true);
        receiveThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                stop();
            }
        });
    }

    protected abstract ReceiveTask newReceiveTask();

    protected abstract String getThreadName();

    protected List<RowMetaData> parseRowMetaData(String value) {
        return JSON.parseArray(value, RowMetaData.class);
    }

    protected abstract class ReceiveTask implements Runnable {
        protected long timeout = PropertyUtil.getLong(options, MessageAwareInstanceConfig.POLL_TIMEOUT);

        protected long period = PropertyUtil.getLong(options, MessageAwareInstanceConfig.POLL_PERIOD);

        protected abstract void closeConsumer();

        protected abstract void handle(List<RowMetaData> rowMetaDataList) throws Exception;
    }

}
