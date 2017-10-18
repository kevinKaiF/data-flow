package com.github.dataflow.sender.core;


import com.github.dataflow.common.model.AbstractDataFlowLifeCycle;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.transformer.core.post.PostDataTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据发送者
 * <p>
 * DataSender是对输出端的抽象，数据接收方可能是Mysql,Oracle等数据库，也可能是
 * 消息队列kafka,MetaQ，可能是Elasticsearch。
 * <p>
 * 一个Instance可能拥有多个DataSender，多个Instance可能拥有相同的DataSender。
 *
 * @author kevin
 * @date 2017-05-29 10:34 PM.
 */
public abstract class DataSender extends AbstractDataFlowLifeCycle {
    private Logger logger = LoggerFactory.getLogger(DataSender.class);

    /**
     * 统计DataSender的引用次数
     */
    private AtomicInteger referenceCount = new AtomicInteger(0);

    /**
     * DataSender的id，对应数据库主键
     */
    protected Long dataSenderId;

    /**
     * DataSender的名称，对应数据库name
     */
    protected String dataSenderName;

    /**
     * DataSender send之前的转换
     */
    protected PostDataTransformer dataTransformer;

    /**
     * 是否是单例
     *
     * @return
     */
    public abstract boolean isSingleton();

    /**
     * 发送数据
     *
     * @param rowMetaDataList
     */
    public void send(List<RowMetaData> rowMetaDataList) throws Exception {
        if (dataTransformer == null) {
            doSend(rowMetaDataList);
        } else {
            Object transformedData = dataTransformer.transform(rowMetaDataList);
            if (transformedData == null) {
                logger.warn("ignore that rowMetaDataList is null after DataTransformer transform rowMetaDataList {}.", rowMetaDataList);
            } else {
                // 如果是Void实例，就不需要再处理了
                if ((!(transformedData instanceof Void))) {
                    doSend(transformedData);
                }
            }
        }
    }

    protected abstract void doSend(Object transformedData) throws Exception;

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
                DataSenderManager.remove(this.getDataSenderId());
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

    public String getDataSenderName() {
        return dataSenderName;
    }

    public void setDataSenderName(String dataSenderName) {
        this.dataSenderName = dataSenderName;
    }

    public PostDataTransformer getDataTransformer() {
        return dataTransformer;
    }

    public void setDataTransformer(PostDataTransformer dataTransformer) {
        this.dataTransformer = dataTransformer;
    }
}
