package com.github.dataflow.sender.database;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.event.EventHandler;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/23
 */
public abstract class DatabaseDataSender extends DataSender {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    protected List<EventHandler> eventHandlers = new ArrayList<>();

    protected DataSourceHolder dataSourceHolder;

    @Override
    public void send(List<RowMetaData> rowMetaDataList) throws Exception {
        for (RowMetaData rowMetaData : rowMetaDataList) {
            for (EventHandler eventHandler : eventHandlers) {
                if (isSupport(rowMetaData, eventHandler)) {
                    try {
                        eventHandler.handle(dataSourceHolder, rowMetaData);
                    } catch (SQLException e) {
                        if (e instanceof MySQLIntegrityConstraintViolationException && e.getMessage().contains("PRIMARY")) {
                            logger.warn("ignore table[{}] 'PRIMARY' violation, rowMetaData : {}", rowMetaData.getFullTableName(), rowMetaData);
                        }
                    }
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

    public void setEventHandlers(List<EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    public DataSourceHolder getDataSourceHolder() {
        return dataSourceHolder;
    }

    public void setDataSourceHolder(DataSourceHolder dataSourceHolder) {
        this.dataSourceHolder = dataSourceHolder;
    }


}
