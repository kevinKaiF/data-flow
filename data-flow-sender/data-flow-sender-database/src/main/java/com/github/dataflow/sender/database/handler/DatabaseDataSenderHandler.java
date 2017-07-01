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

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/23
 */
public abstract class DatabaseDataSenderHandler extends AbstractDataSenderHandler {
    protected static final List<DataSourceType> DATA_SOURCE_TYPEs = new ArrayList<>();

    static {
        DATA_SOURCE_TYPEs.add(DataSourceType.MYSQL);
        DATA_SOURCE_TYPEs.add(DataSourceType.ORACLE);
        DATA_SOURCE_TYPEs.add(DataSourceType.POSTGRESQL);
        DATA_SOURCE_TYPEs.add(DataSourceType.SQLSERVER);
    }

    @Override
    public boolean support(int type) {
        return DATA_SOURCE_TYPEs.contains(DataSourceType.parse(type));
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
