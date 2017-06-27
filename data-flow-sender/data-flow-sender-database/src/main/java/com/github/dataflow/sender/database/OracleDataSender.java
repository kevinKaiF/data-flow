package com.github.dataflow.sender.database;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceOutputType;
import com.github.dataflow.sender.core.event.EventHandler;

//import com.github.dataflow.dubbo.common.enums.DataSourceOutputType;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/24
 */
public class OracleDataSender extends DatabaseDataSender {
    private DataSourceOutputType dataSourceOutputType = DataSourceOutputType.ORACLE;

    @Override
    protected boolean isSupport(RowMetaData rowMetaData, EventHandler eventHandler) {
        return eventHandler.support(dataSourceOutputType, rowMetaData.getEventType());
    }
}
