package com.github.dataflow.sender.hbase.event.handler;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;

/**
 * @author kevin
 * @date 2017-08-27 6:59 PM.
 */
public class HbaseUpdateEventHandler extends HbaseInsertEventHandler {
    @Override
    public boolean support(DataSourceType dataSourceType, RowMetaData.EventType eventType) {
        return dataSourceType == this.dataSourceType && eventType == RowMetaData.EventType.UPDATE;
    }
}
