package com.github.dataflow.sender.elasticsearch.event.handler;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.event.handler.EventHandler;

import java.util.List;

/**
 * @author kevin
 * @date 2017-09-05 11:05 PM.
 */
public class AbstractElasticsearchEventHandler implements EventHandler {
    @Override
    public boolean support(DataSourceType dataSourceType, RowMetaData.EventType eventType) {
        return false;
    }

    @Override
    public void singleHandle(DataSourceHolder dataSourceHolder, RowMetaData rowMetaData) throws Exception {

    }

    @Override
    public void batchHandle(DataSourceHolder dataSourceHolder, List<RowMetaData> rowMetaDataList) throws Exception {

    }
}
