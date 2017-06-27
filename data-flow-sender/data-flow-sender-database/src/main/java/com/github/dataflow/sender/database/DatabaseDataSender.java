package com.github.dataflow.sender.database;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.event.EventHandler;
import com.github.dataflow.sender.core.exception.DataSenderException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/23
 */
public abstract class DatabaseDataSender extends DataSender implements ApplicationContextAware {
    protected List<EventHandler> eventHandlers = new ArrayList<>();

    protected DataSourceHolder dataSourceHolder;

    @Override
    public void send(List<RowMetaData> rowMetaDataList) throws Exception {
        for (RowMetaData rowMetaData : rowMetaDataList) {
            for (EventHandler eventHandler : eventHandlers) {
                if (isSupport(rowMetaData, eventHandler)) {
                    eventHandler.handle(dataSourceHolder, rowMetaData);
                }
            }
        }
    }

    protected abstract boolean isSupport(RowMetaData rowMetaData, EventHandler eventHandler);

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {

    }

    public DataSourceHolder getDataSourceHolder() {
        return dataSourceHolder;
    }

    public void setDataSourceHolder(DataSourceHolder dataSourceHolder) {
        this.dataSourceHolder = dataSourceHolder;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, EventHandler> eventHandlerMap = applicationContext.getBeansOfType(EventHandler.class);
        if (CollectionUtils.isEmpty(eventHandlerMap)) {
            throw new DataSenderException("there is no EventHandler bean");
        } else {
            eventHandlers.clear();
            eventHandlers.addAll(eventHandlerMap.values());
        }
    }
}
