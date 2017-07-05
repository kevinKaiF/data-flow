package com.github.dataflow.sender.database.handler;

import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.AbstractDataSenderHandler;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.sender.database.DatabaseDataSender;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/23
 */
public abstract class DatabaseDataSenderHandler extends AbstractDataSenderHandler {
    protected static final List<DataSourceType> DATA_SOURCE_TYPES = new ArrayList<>();

    static {
        DATA_SOURCE_TYPES.add(DataSourceType.MYSQL);
        DATA_SOURCE_TYPES.add(DataSourceType.ORACLE);
        DATA_SOURCE_TYPES.add(DataSourceType.POSTGRESQL);
        DATA_SOURCE_TYPES.add(DataSourceType.SQLSERVER);
        DATA_SOURCE_TYPES.add(DataSourceType.HIVE);
    }

    @Override
    public boolean support(int type) {
        return DATA_SOURCE_TYPES.contains(DataSourceType.parse(type));
    }

    @Override
    protected Properties refreshDataOutputMapping(DataOutputMapping dataOutputMapping) {
        return null;
    }

    @Override
    protected void afterCreateDataSender(DataSender dataSender, DataOutputMapping dataSourceOutput) {
        if (CollectionUtils.isEmpty(eventHandlers)) {
            throw new DataSenderException("there is no EventHandler bean");
        } else {
            ((DatabaseDataSender) dataSender).setEventHandlers(eventHandlers);
        }
    }
}
