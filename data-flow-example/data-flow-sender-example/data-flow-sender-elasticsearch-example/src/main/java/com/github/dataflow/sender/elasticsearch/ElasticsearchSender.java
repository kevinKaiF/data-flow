package com.github.dataflow.sender.elasticsearch;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.EventDataSender;
import com.github.dataflow.sender.core.event.handler.EventHandler;

/**
 * @author kevin
 * @date 2017-09-05 10:33 PM.
 */
public class ElasticsearchSender extends EventDataSender {
    private DataSourceType dataSourceType = DataSourceType.ELASTICSEARCH;

    @Override
    protected boolean ignoreExceptionAfterSendFailed(Exception e) {
        return true;
    }

    @Override
    protected boolean isSupport(RowMetaData rowMetaData, EventHandler eventHandler) {
        return eventHandler.support(dataSourceType, rowMetaData.getEventType());
    }
}
