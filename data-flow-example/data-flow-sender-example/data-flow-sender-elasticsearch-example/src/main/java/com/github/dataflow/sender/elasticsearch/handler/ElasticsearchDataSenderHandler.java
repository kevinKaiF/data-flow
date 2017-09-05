package com.github.dataflow.sender.elasticsearch.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.event.handler.EventHandler;
import com.github.dataflow.sender.core.handler.EventDataSenderHandler;
import com.github.dataflow.sender.elasticsearch.event.handler.AbstractElasticsearchEventHandler;

/**
 * @author kevin
 * @date 2017-09-05 10:37 PM.
 */
public class ElasticsearchDataSenderHandler extends EventDataSenderHandler {
    private DataSourceType dataSourceType = DataSourceType.ELASTICSEARCH;

    @Override
    public boolean support(int type) {
        return dataSourceType.getType() == type;
    }

    @Override
    protected DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        return null;
    }

    @Override
    protected JSONObject refreshDataOutputMapping(DataOutputMapping dataOutputMapping) {
        return null;
    }

    @Override
    protected Class<? extends EventHandler> getSupportedEventHandler() {
        return AbstractElasticsearchEventHandler.class;
    }
}
