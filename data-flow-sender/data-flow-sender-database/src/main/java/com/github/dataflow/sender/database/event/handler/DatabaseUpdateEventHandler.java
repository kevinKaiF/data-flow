package com.github.dataflow.sender.database.event.handler;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.event.UpdateEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/20
 */
public class DatabaseUpdateEventHandler extends AbstractDatabaseEventHandler implements UpdateEvent {

    @Override
    public boolean support(DataSourceType dataSourceType, RowMetaData.EventType eventType) {
        return DATA_SOURCE_TYPEs.contains(dataSourceType) && UPDATE == eventType;
    }

    @Override
    protected SqlMeta buildSqlMeta(RowMetaData rowMetaData) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(rowMetaData.getFullTableName())
                .append(" SET ");
        List<RowMetaData.ColumnMeta> primaryKeyColumnMetaList = new ArrayList<>();
        List<RowMetaData.ColumnMeta> afterColumns = rowMetaData.getAfterColumns();
        // build set sql
        for (int i = 0, size = afterColumns.size(); i < size; i++) {
            RowMetaData.ColumnMeta columnMeta = afterColumns.get(i);
            if (columnMeta.isKey()) {
                primaryKeyColumnMetaList.add(columnMeta);
            }

            if (i > 0) {
                sb.append(", ");
            }


            sb.append(columnMeta.getColumnName())
                    .append(" = ? ");
        }

        // build where sql
        sb.append(" WHERE ");
        for (int i = 0, size = primaryKeyColumnMetaList.size(); i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(primaryKeyColumnMetaList.get(i).getColumnName())
                    .append(" = ? ");
        }

        // all updated columns, including primary keys
        List<RowMetaData.ColumnMeta> columnMetaList = primaryKeyColumnMetaList;
        columnMetaList.addAll(0, rowMetaData.getAfterColumns());
        return new SqlMeta(sb.toString(), columnMetaList);
    }
}
