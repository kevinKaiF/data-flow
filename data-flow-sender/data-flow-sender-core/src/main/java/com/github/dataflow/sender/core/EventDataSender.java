package com.github.dataflow.sender.core;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.event.handler.EventHandler;
import com.github.dataflow.sender.core.exception.DataSenderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/9/9
 */
public abstract class EventDataSender extends DataSender {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected List<EventHandler> eventHandlers = new ArrayList<>();

    protected DataSourceHolder dataSourceHolder;

    protected boolean batch = true;

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void doSend(Object transformedData) throws Exception {
        List<RowMetaData> rowMetaDataList = (List<RowMetaData>) transformedData;
        List<RowMetaData> batchRowMetaData = new ArrayList<>();
        RowMetaData prev = null;
        for (int i = 0, size = rowMetaDataList.size(); i < size; i++) {
            RowMetaData rowMetaData = rowMetaDataList.get(i);
            boolean handled = false;
            for (EventHandler eventHandler : eventHandlers) {
                if (isSupport(rowMetaData, eventHandler)) {
                    handled = true;
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

            if (!handled) {
                logger.warn("ignore to handle table[{}], rowMetaData : {}", rowMetaData.getFullTableName(), rowMetaData);
            }
        }
    }

    /**
     * 是否支持单个发送
     *
     * @param e
     * @return
     */
    protected abstract boolean supportSingleSend(Exception e);

    private void singleHandle(RowMetaData rowMetaData, EventHandler eventHandler) {
        try {
            eventHandler.singleHandle(dataSourceHolder, rowMetaData);
        } catch (Exception e) {
            if (supportSingleSend(e) && RowMetaData.EventType.INSERT == rowMetaData.getEventType()) {
                // convert to UPDATE
                logger.info("rowMetaData ({}) has existed, convert to UPDATE instead.", rowMetaData);
                rowMetaData.setEventType(RowMetaData.EventType.UPDATE);
                for (EventHandler handler : eventHandlers) {
                    if (isSupport(rowMetaData, handler)) {
                        singleHandle(rowMetaData, handler);
                    }
                }
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
