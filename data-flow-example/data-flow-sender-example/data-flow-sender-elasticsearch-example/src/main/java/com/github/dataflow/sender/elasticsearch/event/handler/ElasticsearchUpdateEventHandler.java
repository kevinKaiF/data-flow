package com.github.dataflow.sender.elasticsearch.event.handler;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;

/**
 * @author kevin
 * @date 2017-09-05 11:10 PM.
 */
public class ElasticsearchUpdateEventHandler extends ElasticsearchInsertEventHandler {
    @Override
    public boolean support(DataSourceType dataSourceType, RowMetaData.EventType eventType) {
        return this.dataSourceType == dataSourceType && eventType == RowMetaData.EventType.UPDATE;
    }
}
