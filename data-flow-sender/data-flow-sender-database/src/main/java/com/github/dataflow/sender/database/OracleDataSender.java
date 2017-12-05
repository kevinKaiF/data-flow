package com.github.dataflow.sender.database;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.event.handler.EventHandler;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/24
 */
public class OracleDataSender extends DatabaseDataSender {
    private DataSourceType dataSourceType = DataSourceType.ORACLE;

    @Override
    protected boolean isSupport(RowMetaData rowMetaData, EventHandler eventHandler) {
        return eventHandler.support(dataSourceType, rowMetaData.getEventType());
    }

    @Override
    protected boolean supportSingleSend(Exception e) {
        return true;
    }
}
