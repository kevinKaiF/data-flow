package com.github.dataflow.sender.database.handler;

import com.github.dataflow.dubbo.common.enums.DataSourceOutputType;
import com.github.dataflow.sender.core.AbstractDataSenderHandler;

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
}
