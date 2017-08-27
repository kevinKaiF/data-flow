package com.github.dataflow.sender.database;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.event.EventHandler;
import com.github.dataflow.sender.core.exception.DataSenderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/23
 */
public abstract class DatabaseDataSender extends DataSender {
    private   Logger             logger        = LoggerFactory.getLogger(this.getClass());
    protected List<EventHandler> eventHandlers = new ArrayList<>();

    protected DataSourceHolder dataSourceHolder;

    protected boolean batch = true;

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void send(List<RowMetaData> rowMetaDataList) throws Exception {
        List<RowMetaData> batchRowMetaData = new ArrayList<>();
        RowMetaData prev = null;
        for (int i = 0, size = rowMetaDataList.size(); i < size; i++) {
            RowMetaData rowMetaData = rowMetaDataList.get(i);
            for (EventHandler eventHandler : eventHandlers) {
                if (isSupport(rowMetaData, eventHandler)) {
                    if (batch) {
                        try {
                            if (dmlForSameTable(prev, rowMetaData)) {
                                prev = rowMetaData;
                                batchRowMetaData.add(rowMetaData);
                            } else {
                                // handle the pre RowMetaData list
                                prev = null;
                                eventHandler.batchHandle(dataSourceHolder, batchRowMetaData);
                                batchRowMetaData.clear();

                                prev = rowMetaData;
                                batchRowMetaData.add(rowMetaData);
                            }

                            // the last RowMetaData
                            if (i == size - 1) {
                                prev = null;
                                eventHandler.batchHandle(dataSourceHolder, batchRowMetaData);
                                batchRowMetaData.clear();
                            }
                        } catch (Exception e) {
                            if (supportSingleSend(e)) {
                                logger.warn("batch handle RowMetaData failure and try to single handle RowMetaData, detail : ", e);
                                for (RowMetaData metaData : batchRowMetaData) {
                                    singleHandle(metaData, eventHandler);
                                }
                            } else {
                                throw new DataSenderException(e);
                            }
                        }
                    } else {
                        singleHandle(rowMetaData, eventHandler);
                    }
                }
            }
        }
    }

    /**
     * 批量处理失败后，是否支持单个处理
     *
     * @param e
     * @return
     */
    protected abstract boolean supportSingleSend(Exception e);

    private void singleHandle(RowMetaData rowMetaData, EventHandler eventHandler) {
        try {
            eventHandler.singleHandle(dataSourceHolder, rowMetaData);
        } catch (Exception e) {
            if (supportSingleSend(e)) {
                logger.warn("ignore to handle table[{}], rowMetaData : {}", rowMetaData.getFullTableName(), rowMetaData);
            } else {
                throw new DataSenderException(e);
            }
        }
    }

    /**
     * 是否是同一个表的同一个dml类型操作
     *
     * @param prev
     * @param current
     * @return
     */
    private boolean dmlForSameTable(RowMetaData prev, RowMetaData current) {
        if (prev == null) {
            return true;
        } else {
            return prev.getFullTableName().equals(current.getFullTableName()) && prev.getEventType().equals(current.getEventType());
        }
    }

    protected abstract boolean isSupport(RowMetaData rowMetaData, EventHandler eventHandler);

    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {

    }

    public void setEventHandlers(List<EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    public DataSourceHolder getDataSourceHolder() {
        return dataSourceHolder;
    }

    public void setDataSourceHolder(DataSourceHolder dataSourceHolder) {
        this.dataSourceHolder = dataSourceHolder;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }
}
