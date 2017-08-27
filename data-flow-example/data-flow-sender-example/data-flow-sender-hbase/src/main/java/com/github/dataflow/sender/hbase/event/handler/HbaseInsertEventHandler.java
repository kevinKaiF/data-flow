package com.github.dataflow.sender.hbase.event.handler;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kevin
 * @date 2017-08-27 6:59 PM.
 */
public class HbaseInsertEventHandler extends AbstractHbaseEventHandler {
    @Override
    public boolean support(DataSourceType dataSourceType, RowMetaData.EventType eventType) {
        return dataSourceType == this.dataSourceType && eventType == RowMetaData.EventType.INSERT;
    }

    @Override
    protected void doSingleHandle(Table table, RowMetaData rowMetaData) throws IOException {
        Put put = handle(rowMetaData);
        table.put(put);
    }

    @Override
    protected void doBatchHandle(Table table, List<RowMetaData> rowMetaDataList) throws IOException {
        List<Put> putList = new ArrayList<>();
        for (RowMetaData rowMetaData : rowMetaDataList) {
            putList.add(handle(rowMetaData));
        }
        table.put(putList);
    }


    private Put handle(RowMetaData rowMetaData) {
        // 获取主键
        Map<String, String> primaryKeys = getPrimaryKeys(rowMetaData, true);
        // 获取rowKey
        String rowKey = getRowKey(primaryKeys);
        Put put = new Put(Bytes.toBytes(rowKey));
        List<RowMetaData.ColumnMeta> afterColumns = rowMetaData.getAfterColumns();
        String tableName = rowMetaData.getTableName();
        for (RowMetaData.ColumnMeta columnMeta : afterColumns) {
            addColumn(put, tableName, columnMeta);
        }
        return put;
    }

    private void addColumn(Put put, String tableName, RowMetaData.ColumnMeta columnMeta) {
        put.addColumn(Bytes.toBytes(tableName), Bytes.toBytes(columnMeta.getColumnName()), Bytes.toBytes(columnMeta.getValue()));
    }

    private String getRowKey(Map<String, String> primaryKeys) {
        // TODO 这里按ID获取，可能需要自定义实现
        // 需要注意：
        // 1.没主键的表
        // 2.联合主键的表
        // 3.主键的大小写问题
        return primaryKeys.get("ID");
    }
}
