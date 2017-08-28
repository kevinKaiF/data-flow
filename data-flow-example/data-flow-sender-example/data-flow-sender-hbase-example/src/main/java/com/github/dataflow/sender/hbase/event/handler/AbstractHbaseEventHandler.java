package com.github.dataflow.sender.hbase.event.handler;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.event.EventHandler;
import com.github.dataflow.sender.core.exception.DataSenderException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由于hbase的列是动态的，所以没法实现定制化的需求。
 * 所以在{@link AbstractHbaseEventHandler#doSingleHandle(Table, RowMetaData)}
 * 还是在{@link AbstractHbaseEventHandler#doSingleHandle(Table, RowMetaData)}
 * 的处理中，有两点需要你自己按照自己的需求实现。
 * 1.RowKey的生成规则
 * 2.hbase的表名与RowMetaData的tableName关系是如何对应的。
 *
 * 本案例仅是一种示范。
 *
 * @author kevin
 * @date 2017-08-27 6:57 PM.
 */
public abstract class AbstractHbaseEventHandler implements EventHandler {
    protected DataSourceType dataSourceType = DataSourceType.HBASE;

    @Override
    public void singleHandle(DataSourceHolder dataSourceHolder, RowMetaData rowMetaData) throws Exception {
        try {
            Connection connection = (Connection) dataSourceHolder.getDataSource();
            // TODO 表名的如何映射的，可能需要自定义实现
            Table table = connection.getTable(TableName.valueOf(rowMetaData.getTableName()));
            doSingleHandle(table, rowMetaData);
        } catch (IOException e) {
            throw new DataSenderException(e);
        }
    }


    protected abstract void doSingleHandle(Table table, RowMetaData rowMetaData) throws IOException;

    @Override
    public void batchHandle(DataSourceHolder dataSourceHolder, List<RowMetaData> rowMetaDataList) throws Exception {
        try {
            Connection connection = (Connection) dataSourceHolder.getDataSource();
            // TODO 表名的如何映射的，可能需要自定义实现
            Table table = connection.getTable(TableName.valueOf(rowMetaDataList.get(0).getTableName()));
            doBatchHandle(table, rowMetaDataList);
        } catch (IOException e) {
            throw new DataSenderException(e);
        }
    }

    protected abstract void doBatchHandle(Table table, List<RowMetaData> rowMetaDataList) throws IOException;

    /**
     * 获取主键
     *
     * @param rowMetaData       row所有数据信息，包括变化前和变化后的字段数据，字段名称
     * @param isBeforeColumns   是否是变化前的数据，比如delete只需要变化前的数据
     * @return
     */
    protected Map<String, String> getPrimaryKeys(RowMetaData rowMetaData, boolean isBeforeColumns) {
        Map<String, String> primaryMap = new HashMap<>();
        if (isBeforeColumns) {
            List<RowMetaData.ColumnMeta> beforeColumns = rowMetaData.getBeforeColumns();
            for (RowMetaData.ColumnMeta columnMeta : beforeColumns) {
                if (columnMeta.isKey()) {
                    primaryMap.put(columnMeta.getColumnName(), columnMeta.getValue());
                }
            }
        } else {
            List<RowMetaData.ColumnMeta> afterColumns = rowMetaData.getAfterColumns();
            for (RowMetaData.ColumnMeta columnMeta : afterColumns) {
                if (columnMeta.isKey()) {
                    primaryMap.put(columnMeta.getColumnName(), columnMeta.getValue());
                }
            }
        }

        return primaryMap;
    }
}
