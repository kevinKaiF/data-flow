package com.github.dataflow.sender.core;


import com.github.dataflow.common.model.AbstractDataFlowLifeCycle;
import com.github.dataflow.common.model.RowMetaData;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据发送者
 * <p>
 * DataSender是对输出端的抽象，数据接收方可能是Mysql,Oracle等数据库，也可能是
 * 消息队列kafka,metaQ，可能是Elasticsearch。
 * <p>
 * 一个Instance可能拥有多个DataSender，多个Instance可能拥有相同的DataSender。
 *
 * @author kevin
 * @date 2017-05-29 10:34 PM.
 */
public abstract class DataSender extends AbstractDataFlowLifeCycle {
    /**
     * 统计DataSender的引用次数
     */
    private AtomicInteger referenceCount = new AtomicInteger(0);

    /**
     * DataSender的id，对应数据库主键
     */
    protected Long dataSenderId;

    /**
     * 发送数据
     *
     * @param rowMetaDataList
     */
    public abstract void send(List<RowMetaData> rowMetaDataList) throws Exception;


    /**
     * 是否是单例
     *
     * @return
     */
    public abstract boolean isSingleton();


    @Override
    public void start() {
        referenceCount.incrementAndGet();
        if (!super.isStart()) {
            super.start();
            doStart();
        }
    }

    @Override
    public void stop() {
        int count = referenceCount.decrementAndGet();
        if (count == 0) {
            if (super.isStart()) {
                super.stop();
                doStop();
            }
        }
    }

    /**
     * 子类实现初始化等操作
     */
    protected abstract void doStart();

    /**
     * 子类实现销毁等操作
     */
    protected abstract void doStop();

    public int getReferenceCount() {
        return referenceCount.get();
    }

    public Long getDataSenderId() {
        return dataSenderId;
    }

    public void setDataSenderId(Long dataSenderId) {
        this.dataSenderId = dataSenderId;
    }

}
