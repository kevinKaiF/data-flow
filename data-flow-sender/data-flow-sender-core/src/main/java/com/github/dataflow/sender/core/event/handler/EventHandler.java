package com.github.dataflow.sender.core.event.handler;


import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;

import java.sql.SQLException;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/20
 */
public interface EventHandler {
    boolean support(DataSourceType dataSourceType, RowMetaData.EventType eventType);

    void singleHandle(DataSourceHolder dataSourceHolder, RowMetaData rowMetaData) throws Exception;

    /**
     * 批处理
     *
     * @param dataSourceHolder
     * @param rowMetaDataList
     * @throws SQLException
     */
    void batchHandle(DataSourceHolder dataSourceHolder, List<RowMetaData> rowMetaDataList) throws Exception;
}
