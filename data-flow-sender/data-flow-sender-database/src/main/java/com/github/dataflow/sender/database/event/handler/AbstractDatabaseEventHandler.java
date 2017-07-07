package com.github.dataflow.sender.database.event.handler;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.Closer;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/20
 */
public abstract class AbstractDatabaseEventHandler implements EventHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static final List<DataSourceType> DATA_SOURCE_TYPEs = new ArrayList<>();

    static {
        DATA_SOURCE_TYPEs.add(DataSourceType.MYSQL);
        DATA_SOURCE_TYPEs.add(DataSourceType.ORACLE);
        DATA_SOURCE_TYPEs.add(DataSourceType.POSTGRESQL);
        DATA_SOURCE_TYPEs.add(DataSourceType.SQLSERVER);
        DATA_SOURCE_TYPEs.add(DataSourceType.HIVE);
    }

    @Override
    public void singleHandle(DataSourceHolder dataSourceHolder, RowMetaData rowMetaData) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            DataSource dataSource = (DataSource) dataSourceHolder.getDataSource();
            connection = dataSource.getConnection();
            SqlMeta sqlMeta = buildSqlMeta(rowMetaData);
            logger.debug("build SqlMeta : {}", sqlMeta);

            preparedStatement = connection.prepareStatement(sqlMeta.getSql());
            populatePrepareStatementParams(preparedStatement, sqlMeta);
            preparedStatement.execute();
        } finally {
            Closer.closeQuietly(preparedStatement);
            Closer.closeQuietly(connection);
        }
    }

    @Override
    public void batchHandle(DataSourceHolder dataSourceHolder, List<RowMetaData> rowMetaDataList) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            DataSource dataSource = (DataSource) dataSourceHolder.getDataSource();
            connection = dataSource.getConnection();
            for (RowMetaData rowMetaData : rowMetaDataList) {
                SqlMeta sqlMeta = buildSqlMeta(rowMetaData);
                logger.debug("build SqlMeta : {}", sqlMeta);

                if (preparedStatement == null) {
                    preparedStatement = connection.prepareStatement(sqlMeta.getSql());
                }
                populatePrepareStatementParams(preparedStatement, sqlMeta);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } finally {
            Closer.closeQuietly(preparedStatement);
            Closer.closeQuietly(connection);
        }
    }

    /**
     * 填充sql执行的参数
     *
     * @param preparedStatement
     * @param sqlMeta
     * @throws SQLException
     */
    private void populatePrepareStatementParams(PreparedStatement preparedStatement, SqlMeta sqlMeta) throws SQLException {
        List<RowMetaData.ColumnMeta> columnMetaList = sqlMeta.getColumnMetaList();
        if (!CollectionUtils.isEmpty(columnMetaList)) {
            for (int i = 0, size = columnMetaList.size(); i < size; i++) {
                RowMetaData.ColumnMeta columnMeta = columnMetaList.get(i);
                preparedStatement.setObject(i + 1, columnMeta.getValue(), columnMeta.getJdbcType());
            }
        }
    }

    /**
     * 构建可执行的sql及参数
     *
     * @param rowMetaData
     * @return
     */
    protected abstract SqlMeta buildSqlMeta(RowMetaData rowMetaData);

    protected class SqlMeta {
        private String                       sql;
        private List<RowMetaData.ColumnMeta> columnMetaList;

        public SqlMeta(String sql, List<RowMetaData.ColumnMeta> columnMetaList) {
            this.sql = sql;
            this.columnMetaList = columnMetaList;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public void setColumnMetaList(List<RowMetaData.ColumnMeta> columnMetaList) {
            this.columnMetaList = columnMetaList;
        }

        public String getSql() {
            return sql;
        }

        public List<RowMetaData.ColumnMeta> getColumnMetaList() {
            return columnMetaList;
        }

        @Override
        public String toString() {
            return "SqlMeta{" +
                    "sql='" + sql + '\'' +
                    ", columnMetaList=" + columnMetaList +
                    '}';
        }
    }
}
