package com.github.dataflow.sender.database;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.EventDataSender;
import com.github.dataflow.sender.core.event.handler.EventHandler;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.BatchUpdateException;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/24
 */
public class MysqlDataSender extends EventDataSender {
    private DataSourceType dataSourceType = DataSourceType.MYSQL;

    @Override
    protected boolean isSupport(RowMetaData rowMetaData, EventHandler eventHandler) {
        return eventHandler.support(dataSourceType, rowMetaData.getEventType());
    }

    @Override
    protected boolean supportSingleSend(Exception e) {
        return e instanceof BatchUpdateException || (e instanceof MySQLIntegrityConstraintViolationException && e.getMessage().contains("PRIMARY"));
    }
}
