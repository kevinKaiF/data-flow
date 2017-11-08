package com.github.dataflow.sender.database.event.handler.mysql;


import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.database.event.handler.DatabaseInsertEventHandler;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/11/2
 */
public class MySqlInsertEventHandler extends DatabaseInsertEventHandler {
    @Override
    public boolean support(DataSourceType dataSourceType, RowMetaData.EventType eventType) {
        return dataSourceType == DataSourceType.MYSQL && eventType == RowMetaData.EventType.INSERT;
    }

    @Override
    protected SqlMeta buildSqlMeta(RowMetaData rowMetaData) {
        SqlMeta sqlMeta = super.buildSqlMeta(rowMetaData);
        StringBuilder sqlBuilder = new StringBuilder(sqlMeta.getSql());
        List<RowMetaData.ColumnMeta> columnMetaList = sqlMeta.getColumnMetaList();
        sqlBuilder.append(" ON DUPLICATE KEY UPDATE ");
        int size = columnMetaList.size();
        for (int i = 0; i < size; i++) {
            RowMetaData.ColumnMeta columnMeta = columnMetaList.get(i);
            if (i > 0) {
                sqlBuilder.append(" , ");
            }
            sqlBuilder.append(columnMeta.getColumnName()).append("=").append("?");
        }

        columnMetaList.addAll(columnMetaList);
        return new SqlMeta(sqlBuilder.toString(), columnMetaList);
    }
}
