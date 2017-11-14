package com.github.dataflow.node.model.instance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.JSONObjectUtil;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/5
 */
public abstract class AbstractPooledInstance extends AbstractInstance {
    protected Semaphore semaphore = new Semaphore(0);

    protected Thread receiveThread;

    protected JSONObject options;

    public AbstractPooledInstance() {
    }

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
                // TODO
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
        protected long timeout = JSONObjectUtil.getLong(options, PooledInstanceConfig.POLL_TIMEOUT);

        protected long period = JSONObjectUtil.getLong(options, PooledInstanceConfig.POLL_PERIOD);

        protected abstract void closeConsumer();

        protected abstract void handle(List<RowMetaData> rowMetaDataList) throws Exception;
    }

}
