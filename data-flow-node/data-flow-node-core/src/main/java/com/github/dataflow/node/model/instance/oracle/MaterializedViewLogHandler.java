package com.github.dataflow.node.model.instance.oracle;


import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.Closer;
import com.github.dataflow.sender.core.utils.DBUtil;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/11/8
 */
public class MaterializedViewLogHandler {
    private static final String CREATE_VIEW_LOG = "CREATE MATERIALIZED VIEW LOG ON {0}.{1} WITH ROWID, SEQUENCE({2}) INCLUDING NEW VALUES ";

    private static final String SELECT_VIEW_LOG = "SELECT ROWID,DMLTYPE$$,SEQUENCE$$,M_ROW$$,OLD_NEW$$,{0} FROM {1}.{2} WHERE ROWNUM <= {3} ORDER BY SEQUENCE$$ ASC";

    private static final String DELETE_VIEW_LOG = "DELETE FROM {0}.{1} WHERE ROWID in ({2})";

    private static final String QUERY_VIEW_LOG = "SELECT MASTER,LOG_TABLE FROM ALL_MVIEW_LOGS WHERE MASTER = ? AND LOG_OWNER = ?";

    // 物化日志不支持的类型
    private static Map<Integer, Integer> blackJdbcType = new HashMap<>();

    private ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    private int queryPageSize;

    private DataSource oracleDataSource;

    static {
        blackJdbcType.put(Types.BLOB, Types.BLOB);
        blackJdbcType.put(Types.CLOB, Types.CLOB);
        blackJdbcType.put(Types.NCLOB, Types.NCLOB);
        blackJdbcType.put(Types.BINARY, Types.BINARY);
        blackJdbcType.put(Types.VARBINARY, Types.VARBINARY);
        blackJdbcType.put(Types.LONGVARBINARY, Types.LONGVARBINARY);
    }

    public MaterializedViewLogHandler(DataSource oracleDataSource, int queryPageSize) {
        this.oracleDataSource = oracleDataSource;
        this.queryPageSize = queryPageSize;
    }

    public QueryViewLogCallback.RowMetaDataHolder queryViewLog(TableMeta tableMeta) throws SQLException {
        Connection connection = getConnection();
        if (!tableMeta.hasViewLog()) {
            List<Object> params = new ArrayList<>();
            params.add(tableMeta.getTable());
            params.add(tableMeta.getSchema());
            List<Map<String, Object>> viewLogList = DBUtil.query(connection, QUERY_VIEW_LOG, params);
            // 没有物化日志
            if (CollectionUtils.isEmpty(viewLogList)) {
                String columns = tableMeta.getWhiteColumnsString();
                String createSql = MessageFormat.format(CREATE_VIEW_LOG, tableMeta.getSchema(), tableMeta.getTable(), columns);
                DBUtil.execute(connection, createSql);
            }

            // 再查询一次
            viewLogList = DBUtil.query(connection, QUERY_VIEW_LOG, params);
            String viewLogName = ((String) viewLogList.get(0).get("LOG_TABLE"));
            tableMeta.setViewLogName(viewLogName);
        }

        String selectSql = MessageFormat.format(SELECT_VIEW_LOG, tableMeta.getWhiteColumnsString(), tableMeta.getSchema(), tableMeta.getViewLogName(), queryPageSize);
        QueryViewLogCallback.RowMetaDataHolder metaDataHolder = DBUtil.query(connection, selectSql, null, new QueryViewLogCallback(tableMeta, connection));
        return metaDataHolder;
    }

    public TableMeta queryTableMeta(String fullTableName) throws SQLException {
        Connection connection = getConnection();
        ResultSet resultSet = null;
        ResultSet primaryKeysSet = null;
        try {
            String[] split = fullTableName.split("\\.");
            final String schema = split[0];
            final String tableName = split[1];
            DatabaseMetaData metaData = connection.getMetaData();
            // 获取列信息
            resultSet = metaData.getColumns(schema, null, tableName, null);
            List<RowMetaData.ColumnMeta> fullColumnMetaList = new ArrayList<>();
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                int jdbcType = resultSet.getInt("DATA_TYPE");
                RowMetaData.ColumnMeta columnMeta = new RowMetaData.ColumnMeta();
                columnMeta.setColumnName(columnName);
                columnMeta.setJdbcType(jdbcType);
                fullColumnMetaList.add(columnMeta);
            }
            // 获取主键信息
            primaryKeysSet = metaData.getPrimaryKeys(schema, schema, tableName);
            Map<String, Object> primaryKeyMap = new HashMap<>();
            while (primaryKeysSet.next()) {
                String columnName = primaryKeysSet.getString("COLUMN_NAME");
                primaryKeyMap.put(columnName, columnName);
            }

            List<RowMetaData.ColumnMeta> whiteColumns = new ArrayList<>();
            List<RowMetaData.ColumnMeta> blackColumns = new ArrayList<>();
            for (RowMetaData.ColumnMeta columnMeta : fullColumnMetaList) {
                // 主键
                if (primaryKeyMap.get(columnMeta.getColumnName()) != null) {
                    columnMeta.setKey(true);
                }

                if (blackJdbcType.get(columnMeta.getJdbcType()) == null) {
                    whiteColumns.add(columnMeta);
                } else {
                    blackColumns.add(columnMeta);
                }
            }

            return new TableMeta(schema, tableName, fullColumnMetaList, blackColumns, whiteColumns);
        } finally {
            Closer.closeQuietly(resultSet);
            Closer.closeQuietly(primaryKeysSet);
        }
    }

    public void deleteViewLog(TableMeta tableMeta, List<Object> rowIds) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = getConnection();
            int size = rowIds.size();
            StringBuilder placeHolders = new StringBuilder();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    placeHolders.append(", ");
                }
                placeHolders.append("?");
            }
            String deleteSql = MessageFormat.format(DELETE_VIEW_LOG, tableMeta.getSchema(), tableMeta.getViewLogName(), placeHolders.toString());
            DBUtil.execute(connection, deleteSql, rowIds);
        } finally {
            Closer.closeQuietly(preparedStatement);
        }
    }

    private Connection getConnection() throws SQLException {
        Connection connection = connectionHolder.get();
        if (connection == null) {
            connection = oracleDataSource.getConnection();
            connectionHolder.set(connection);
        }
        return connectionHolder.get();
    }

    class TableMeta {
        // 库名
        private String                       schema;
        // 表名
        private String                       table;
        // 所有的列
        private List<RowMetaData.ColumnMeta> fullColumns;
        // 不支持物化日志的列
        private List<RowMetaData.ColumnMeta> blackColumns;
        // 支持物化日志的列
        private List<RowMetaData.ColumnMeta> whiteColumns;
        // 物化日志名称
        private String viewLogName;
        // 物化日志所有列
        private String whiteColumnString;
        // 不支持物化日志的列
        private String blackColumnString;


        public TableMeta(String schema, String table, List<RowMetaData.ColumnMeta> fullColumns) {
            this.schema = schema;
            this.table = table;
            this.fullColumns = fullColumns;
        }

        public TableMeta(String schema, String table, List<RowMetaData.ColumnMeta> fullColumns, List<RowMetaData.ColumnMeta> blackColumns, List<RowMetaData.ColumnMeta> whiteColumns) {
            this.schema = schema;
            this.table = table;
            this.fullColumns = fullColumns;
            this.blackColumns = blackColumns;
            this.whiteColumns = whiteColumns;
        }

        public boolean hasBlackColumn() {
            return blackColumns != null && !blackColumns.isEmpty();
        }

        public boolean hasViewLog() {
            return viewLogName != null;
        }


        public String getWhiteColumnsString() {
            if (whiteColumnString == null) {
                whiteColumnString = doGetColumnsString(whiteColumns);
                return whiteColumnString;
            } else {
                return whiteColumnString;
            }
        }

        public String getBlackColumnString() {
            if (blackColumnString == null) {
                blackColumnString = doGetColumnsString(blackColumns);
                return blackColumnString;
            } else {
                return blackColumnString;
            }
        }

        private String doGetColumnsString(List<RowMetaData.ColumnMeta> columns) {
            int size = columns.size();
            StringBuilder columnBuilder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    columnBuilder.append(", ");
                }
                columnBuilder.append(columns.get(i).getColumnName());
            }

            return columnBuilder.toString();
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public List<RowMetaData.ColumnMeta> getFullColumns() {
            return fullColumns;
        }

        public void setFullColumns(List<RowMetaData.ColumnMeta> fullColumns) {
            this.fullColumns = fullColumns;
        }

        public List<RowMetaData.ColumnMeta> getBlackColumns() {
            return blackColumns;
        }

        public void setBlackColumns(List<RowMetaData.ColumnMeta> blackColumns) {
            this.blackColumns = blackColumns;
        }

        public List<RowMetaData.ColumnMeta> getWhiteColumns() {
            return whiteColumns;
        }

        public void setWhiteColumns(List<RowMetaData.ColumnMeta> whiteColumns) {
            this.whiteColumns = whiteColumns;
        }

        public String getViewLogName() {
            return viewLogName;
        }

        public void setViewLogName(String viewLogName) {
            this.viewLogName = viewLogName;
        }
    }
}
