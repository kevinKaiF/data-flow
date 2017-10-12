package com.github.dataflow.sender.database.event.handler;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.event.DeleteEvent;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/20
 */
public class DatabaseDeleteEventHandler extends AbstractDatabaseEventHandler implements DeleteEvent {

    @Override
    public boolean support(DataSourceType dataSourceType, RowMetaData.EventType eventType) {
        return DATA_SOURCE_TYPEs.contains(dataSourceType) && DELETE == eventType;
    }

    @Override
    protected SqlMeta buildSqlMeta(RowMetaData rowMetaData) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(rowMetaData.getFullTableName());
        List<RowMetaData.ColumnMeta> primaryKeyColumnMeta = new ArrayList<>();
        for (RowMetaData.ColumnMeta beforeColumn : rowMetaData.getBeforeColumns()) {
            if (beforeColumn.isKey()) {
                primaryKeyColumnMeta.add(beforeColumn);
            }
        }

        if (!CollectionUtils.isEmpty(primaryKeyColumnMeta)) {
            sb.append(" WHERE ");
            for (int i = 0, size = primaryKeyColumnMeta.size(); i < size; i++) {
                if (i > 0) {
                    sb.append("AND ");
                }
                sb.append(primaryKeyColumnMeta.get(i).getColumnName()).append(" = ? ");
            }
        }

        return new SqlMeta(sb.toString(), primaryKeyColumnMeta);
    }
}
