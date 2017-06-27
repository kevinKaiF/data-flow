package com.github.dataflow.sender.core.event;


import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceOutputType;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;

import java.sql.SQLException;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/20
 */
public interface EventHandler {
    boolean support(DataSourceOutputType dataSourceOutputType, RowMetaData.EventType eventType);

    void handle(DataSourceHolder dataSourceHolder, RowMetaData rowMetaData) throws SQLException;
}
