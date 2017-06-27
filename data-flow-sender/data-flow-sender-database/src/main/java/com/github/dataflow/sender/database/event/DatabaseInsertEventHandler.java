package com.github.dataflow.sender.database.event;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceOutputType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/20
 */
@Component
public class DatabaseInsertEventHandler extends AbstractDatabaseEventHandler {
    private RowMetaData.EventType eventType = RowMetaData.EventType.INSERT;

    @Override
    public boolean support(DataSourceOutputType dataSourceOutputType, RowMetaData.EventType eventType) {
        return dataSourceOutputTypes.contains(dataSourceOutputType) && this.eventType == eventType;
    }

    @Override
    protected SqlMeta buildSqlMeta(RowMetaData rowMetaData) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ")
                .append(rowMetaData.getFullTableName())
                .append(" ( ");
        List<RowMetaData.ColumnMeta> afterColumns = rowMetaData.getAfterColumns();
        // build columns
        for (int i = 0, size = afterColumns.size(); i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(afterColumns.get(i).getColumnName());
        }

        // build values
        sb.append(" ) VALUES (");
        for (int i = 0, size = afterColumns.size(); i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(" ? ");
        }
        sb.append(")");
        return new SqlMeta(sb.toString(), rowMetaData.getAfterColumns());
    }
}
