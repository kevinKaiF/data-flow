package com.github.dataflow.sender.database.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.EventDataSender;
import com.github.dataflow.sender.core.event.handler.EventHandler;
import com.github.dataflow.sender.core.handler.EventDataSenderHandler;
import com.github.dataflow.sender.database.config.DatabaseConfig;
import com.github.dataflow.sender.database.event.handler.AbstractDatabaseEventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/23
 */
public abstract class AbstractDatabaseDataSenderHandler extends EventDataSenderHandler {
    protected static final List<DataSourceType> DATA_SOURCE_TYPES = new ArrayList<>();

    static {
        DATA_SOURCE_TYPES.add(DataSourceType.MYSQL);
        DATA_SOURCE_TYPES.add(DataSourceType.ORACLE);
        DATA_SOURCE_TYPES.add(DataSourceType.POSTGRESQL);
        DATA_SOURCE_TYPES.add(DataSourceType.SQLSERVER);
    }

    @Override
    public boolean support(int type) {
        return DATA_SOURCE_TYPES.contains(DataSourceType.parse(type));
    }

    @Override
    protected JSONObject refreshDataOutputMapping(DataOutputMapping dataOutputMapping) {
        return null;
    }

    @Override
    protected void afterCreateDataSender(DataSender dataSender, DataOutputMapping dataOutputMapping) {
        super.afterCreateDataSender(dataSender, dataOutputMapping);
        // set batch
        JSONObject properties = JSONObjectUtil.parseJSON(dataOutputMapping.getDataSourceOutput().getOptions());
        EventDataSender eventDataSender = (EventDataSender) dataSender;
        eventDataSender.setBatch(JSONObjectUtil.getBoolean(properties, DatabaseConfig.MappingConfig.BATCH, Boolean.TRUE));
    }

    @Override
    protected Class<? extends EventHandler> getSupportedEventHandler() {
        return AbstractDatabaseEventHandler.class;
    }
}
