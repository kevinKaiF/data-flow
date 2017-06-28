package com.github.dataflow.sender.database.handler;

import com.github.dataflow.dubbo.common.enums.DataSourceOutputType;
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
    protected static final List<DataSourceOutputType> dataSourceOutputTypes = new ArrayList<>();

    static {
        dataSourceOutputTypes.add(DataSourceOutputType.MYSQL);
        dataSourceOutputTypes.add(DataSourceOutputType.ORACLE);
        dataSourceOutputTypes.add(DataSourceOutputType.POSTGRESQL);
        dataSourceOutputTypes.add(DataSourceOutputType.SQLSERVER);
    }

    @Override
    public boolean support(int type) {
        return dataSourceOutputTypes.contains(DataSourceOutputType.parse(type));
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
