package com.github.dataflow.node.model.instance.oracle;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.node.exception.InstanceException;
import com.github.dataflow.sender.core.utils.DBUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/11/9
 */
public class QueryViewLogCallback implements DBUtil.ResultSetCallback<QueryViewLogCallback.RowMetaDataHolder> {
    private static final String QUERY_BY_SEQUENCE$$ = "SELECT ROWID,{0} FROM {1}.{2} WHERE SEQUENCE$$ = {3}";

    private static final String QUERY_SOURCE_BY_ROWID = "SELECT {0} FROM {1}.{2} WHERE ROWID = ?";

    private static String TIME_STAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static Logger logger = LoggerFactory.getLogger(OracleInstance.class);

    private MaterializedViewLogHandler.TableMeta tableMeta;

    private Connection connection;

    public QueryViewLogCallback(MaterializedViewLogHandler.TableMeta tableMeta, Connection connection) {
        this.tableMeta = tableMeta;
        this.connection = connection;
    }

    @Override
    public RowMetaDataHolder execute(ResultSet resultSet) throws SQLException {
        List<RowMetaData> rowMetaDataList = new ArrayList<>();
        List<Object> rowIds = new ArrayList<>();
        while (resultSet.next()) {
            RowMetaData.EventType eventType = getDmlType(resultSet);
            RowMetaData rowMetaData = null;
            switch (eventType) {
                case INSERT:
                    rowMetaData = buildInsertRecord(resultSet, tableMeta, rowIds);
                    break;
                case UPDATE:
                    rowMetaData = buildUpdateRecord(resultSet, tableMeta, rowIds);
                    break;
                case DELETE:
                    rowMetaData = buildDeleteRecord(resultSet, tableMeta, rowIds);
                    break;
            }

            // 处理下不支持物化日志的字段
            if (tableMeta.hasBlackColumn()) {
                if (eventType != RowMetaData.EventType.DELETE) {
                    List<RowMetaData.ColumnMeta> blackColumns = queryBlackColumnData(getM_Row$$(resultSet));
                    rowMetaData.getAfterColumns().addAll(blackColumns);
                }
            }

            if (rowMetaData != null) {
                rowMetaDataList.add(rowMetaData);
            }
        }

        return new RowMetaDataHolder(rowMetaDataList, rowIds);
    }

    private List<RowMetaData.ColumnMeta> queryBlackColumnData(List<Object> params) throws SQLException {
        String queryBlackColumnDataSql = MessageFormat.format(QUERY_SOURCE_BY_ROWID, tableMeta.getBlackColumnString(), tableMeta.getSchema(), tableMeta.getTable());
        return DBUtil.query(connection, queryBlackColumnDataSql, params, new DBUtil.ResultSetCallback<List<RowMetaData.ColumnMeta>>() {
            @Override
            public List<RowMetaData.ColumnMeta> execute(ResultSet resultSet) throws SQLException {
                List<RowMetaData.ColumnMeta> columnMetaList = new ArrayList<>();
                if (resultSet.next()) {
                    List<RowMetaData.ColumnMeta> blackColumns = tableMeta.getBlackColumns();
                    for (RowMetaData.ColumnMeta blackColumn : blackColumns) {
                        RowMetaData.ColumnMeta columnMeta = blackColumn.clone();
                        parseColumnValue(resultSet, columnMeta);
                        columnMetaList.add(columnMeta);
                    }
                }
                return columnMetaList;
            }
        });
    }

    /**
     * 获取物化日志中的M_ROW$$字段，对应源表的rowId
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private List<Object> getM_Row$$(ResultSet resultSet) throws SQLException {
        List<Object> params = new ArrayList<>();
        // 获取源表的rowId
        params.add(resultSet.getObject("M_ROW$$"));
        return params;
    }

    private RowMetaData buildDeleteRecord(ResultSet resultSet, MaterializedViewLogHandler.TableMeta tableMeta, List<Object> rowIds) throws SQLException {
        List<RowMetaData.ColumnMeta> columns = buildColumnMeta(resultSet, tableMeta.getWhiteColumns());
        RowMetaData rowMetaData = new RowMetaData();
        rowMetaData.setBeforeColumns(columns);
        rowMetaData.setEventType(RowMetaData.EventType.DELETE);
        rowMetaData.setSchemaName(tableMeta.getSchema());
        rowMetaData.setTableName(tableMeta.getTable());
        rowIds.add(getRowId(resultSet));
        return rowMetaData;
    }

    private List<RowMetaData.ColumnMeta> buildColumnMeta(ResultSet resultSet, List<RowMetaData.ColumnMeta> whiteColumns) throws SQLException {
        List<RowMetaData.ColumnMeta> columnValues = new LinkedList<>();
        for (RowMetaData.ColumnMeta whiteColumn : whiteColumns) {
            RowMetaData.ColumnMeta columnMeta = whiteColumn.clone();
            parseColumnValue(resultSet, columnMeta);
            columnValues.add(columnMeta);
        }
        return columnValues;
    }

    /**
     * note:处理不支持物化日志字段
     *
     * @param resultSet
     * @param columnMeta
     * @throws SQLException
     */
    private void parseColumnValue(ResultSet resultSet, RowMetaData.ColumnMeta columnMeta) throws SQLException {
        String value = null;
        String columnName = columnMeta.getColumnName();
        switch (columnMeta.getJdbcType()) {
            case Types.TIMESTAMP:
                Timestamp timestamp = resultSet.getTimestamp(columnName);
                value = timestamp == null ? null : String.valueOf(new DateTime(timestamp.getTime()).toString(TIME_STAMP_PATTERN));
                break;
            case Types.CLOB:
            case Types.NCLOB:
                value = getClobColumnValue(resultSet, columnName);
                break;
            case Types.BLOB:
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                value = getBlobColumnValue(resultSet, columnName);
                break;
            default:
                Object object = resultSet.getObject(columnName);
                value = object == null ? null : String.valueOf(object);
        }
        columnMeta.setValue(value);
    }

    /**
     * 获取clob字段数据
     *
     * @param resultSet
     * @param columnName
     * @return
     * @throws SQLException
     */
    private String getClobColumnValue(ResultSet resultSet, String columnName) throws SQLException {
        Reader reader = null;
        try {
            reader = resultSet.getCharacterStream(columnName);
            return (reader == null ? null :  IOUtils.toString(reader));
        } catch (IOException e) {
            throw new InstanceException("caught exception when get the black column[" + columnName + "] value, detail:", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * 获取blob字段数据
     *
     * @param resultSet
     * @param columnName
     * @return
     * @throws SQLException
     */
    private String getBlobColumnValue(ResultSet resultSet, String columnName) throws SQLException {
        InputStream inputStream = null;
        try {
            inputStream = resultSet.getBinaryStream(columnName);
            return (inputStream == null ? null :  IOUtils.toString(inputStream));
        } catch (IOException e) {
            throw new InstanceException("caught exception when get the black column[" + columnName + "] value, detail:", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private RowMetaData buildUpdateRecord(ResultSet resultSet, MaterializedViewLogHandler.TableMeta tableMeta, List<Object> rowIds) throws SQLException {
        rowIds.add(getRowId(resultSet));
        // 数据更改标志位，O或U表示变更前的老数据，N表示变更后的新数据
        String oldOrNew = resultSet.getString("OLD_NEW$$");
        if (StringUtils.equals("N", oldOrNew)) {
            List<RowMetaData.ColumnMeta> afterColumns = buildColumnMeta(resultSet, tableMeta.getWhiteColumns());
            List<RowMetaData.ColumnMeta> beforeColumns = getPrevUpdateColumnMeta(resultSet, tableMeta, rowIds);

            if (CollectionUtils.isEmpty(beforeColumns)) {
                logger.warn("the OLD_NEW$$ = 'N' of {}.MLOG$_{} without sibling causes that will be ignored ", tableMeta.getSchema(), tableMeta.getTable());
                return null;
            } else {
                RowMetaData rowMetaData = new RowMetaData();
                rowMetaData.setBeforeColumns(beforeColumns);
                rowMetaData.setAfterColumns(afterColumns);
                rowMetaData.setEventType(RowMetaData.EventType.UPDATE);
                rowMetaData.setSchemaName(tableMeta.getSchema());
                rowMetaData.setTableName(tableMeta.getTable());
                return rowMetaData;
            }
        } else {  // O或者U
            List<RowMetaData.ColumnMeta> beforeColumns = buildColumnMeta(resultSet, tableMeta.getWhiteColumns());
            List<RowMetaData.ColumnMeta> afterColumns = getNextUpdateColumnMeta(resultSet, tableMeta, rowIds);

            if (CollectionUtils.isEmpty(afterColumns)) {
                logger.warn("the OLD_NEW$$ = 'O' of {}.MLOG$_{} without sibling causes that will be ignored ", tableMeta.getSchema(), tableMeta.getTable());
                return null;
            } else {
                RowMetaData rowMetaData = new RowMetaData();
                rowMetaData.setBeforeColumns(beforeColumns);
                rowMetaData.setAfterColumns(afterColumns);
                rowMetaData.setEventType(RowMetaData.EventType.UPDATE);
                rowMetaData.setSchemaName(tableMeta.getSchema());
                rowMetaData.setTableName(tableMeta.getTable());
                return rowMetaData;
            }
        }
    }

    /**
     * 获取UPDATE中上一条mlog日志
     */
    private List<RowMetaData.ColumnMeta> getPrevUpdateColumnMeta(ResultSet resultSet, MaterializedViewLogHandler.TableMeta tableMeta, List<Object> rowIds) throws SQLException {
        int sequence = resultSet.getInt("SEQUENCE$$");
        List<RowMetaData.ColumnMeta> columnMetaList = getSingleMlogRecord(tableMeta, sequence, rowIds);
        return columnMetaList;
    }

    private List<RowMetaData.ColumnMeta> getSingleMlogRecord(final MaterializedViewLogHandler.TableMeta tableMeta, int sequence, final List<Object> rowIds) {
        String querySql = MessageFormat.format(QUERY_BY_SEQUENCE$$, tableMeta.getWhiteColumnsString(), tableMeta.getSchema(), tableMeta.getTable(), sequence - 1);
        return DBUtil.query(connection, querySql, null, new DBUtil.ResultSetCallback<List<RowMetaData.ColumnMeta>>() {
            @Override
            public List<RowMetaData.ColumnMeta> execute(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    rowIds.add(getRowId(resultSet));
                    return buildColumnMeta(resultSet, tableMeta.getWhiteColumns());
                } else {
                    return null;
                }
            }
        });
    }

    private Object getRowId(ResultSet resultSet) throws SQLException {
        return resultSet.getObject("ROWID");
    }

    /**
     * 获取UPDATE中下一条mlog日志
     *
     * @param resultSet
     * @param rowIds
     * @return
     */
    private List<RowMetaData.ColumnMeta> getNextUpdateColumnMeta(ResultSet resultSet, MaterializedViewLogHandler.TableMeta tableMeta, List<Object> rowIds) throws SQLException {
        ResultSet curr = resultSet;
        int sequence = curr.getInt("SEQUENCE$$");
        if (curr.next() && curr.getInt("SEQUENCE$$") == (sequence + 1)) {
            rowIds.add(getRowId(curr));
            return buildColumnMeta(curr, tableMeta.getWhiteColumns());
        } else {
            return getSingleMlogRecord(tableMeta, sequence, rowIds);
        }
    }

    private RowMetaData buildInsertRecord(ResultSet resultSet, MaterializedViewLogHandler.TableMeta tableMeta, List<Object> rowIds) throws SQLException {
        List<RowMetaData.ColumnMeta> columns = buildColumnMeta(resultSet, tableMeta.getWhiteColumns());
        RowMetaData rowMetaData = new RowMetaData();
        rowMetaData.setAfterColumns(columns);
        rowMetaData.setEventType(RowMetaData.EventType.INSERT);
        rowMetaData.setSchemaName(tableMeta.getSchema());
        rowMetaData.setTableName(tableMeta.getTable());
        rowIds.add(getRowId(resultSet));
        return rowMetaData;
    }

    private RowMetaData.EventType getDmlType(ResultSet resultSet) throws SQLException {
        String dmlType = resultSet.getString("DMLTYPE$$");
        if ("I".equals(dmlType)) {
            return RowMetaData.EventType.INSERT;
        } else if ("D".equals(dmlType)) {
            return RowMetaData.EventType.DELETE;
        } else {
            return RowMetaData.EventType.UPDATE;
        }
    }


    class RowMetaDataHolder {
        private List<RowMetaData> rowMetaDataList;
        private List<Object>      rowIds;

        public RowMetaDataHolder(List<RowMetaData> rowMetaDataList, List<Object> rowIds) {
            this.rowMetaDataList = rowMetaDataList;
            this.rowIds = rowIds;
        }

        public List<RowMetaData> getRowMetaDataList() {
            return rowMetaDataList;
        }

        public List<Object> getRowIds() {
            return rowIds;
        }
    }
}
