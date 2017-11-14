package com.github.dataflow.common.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取数据库的元信息
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/11/13
 */
public class DBMetaUtil {
    /**
     * 获取指定表的所有列
     *
     * @param connection
     * @param schema
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static List<String> getColumns(Connection connection, String schema, String tableName) throws SQLException {
        ResultSet resultSet = null;
        List<String> columns;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            resultSet = metaData.getColumns(schema, null, tableName, null);
            columns = new ArrayList<>();
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                columns.add(columnName);
            }
            return columns;
        } finally {
            Closer.closeQuietly(resultSet);
        }
    }

    /**
     * 获取指定表的所有主键
     *
     * @param connection
     * @param catalog
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static List<String> getPrimaryKeys(Connection connection, String catalog, String tableName) throws SQLException {
        ResultSet primaryKeysSet = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            primaryKeysSet = metaData.getPrimaryKeys(catalog, catalog, tableName);
            List<String> primaryKeys = new ArrayList<>();
            while (primaryKeysSet.next()) {
                String columnName = primaryKeysSet.getString("COLUMN_NAME");
                primaryKeys.add(columnName);
            }
            return primaryKeys;
        } finally {
            Closer.closeQuietly(primaryKeysSet);
        }
    }


    /**
     * 对数据库大小写敏感做处理
     *
     * @param name
     * @param metaData
     * @return
     * @throws SQLException
     */
    public static String getCanonicalName(String name, DatabaseMetaData metaData) throws SQLException {
        if (metaData.storesUpperCaseIdentifiers()) {
            return name.toUpperCase();
        } else if (metaData.storesLowerCaseIdentifiers()) {
            return name.toLowerCase();
        } else {
            return name;
        }
    }


    /**
     * 获取指定表的所有schema
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    public static List<String> getSchemaNameList(Connection connection) throws SQLException {
        ResultSet resultSet = null;
        List<String> schemaList = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            String driverName = metaData.getDriverName();
            if (driverName.indexOf("MySQL") > -1) {
                resultSet = metaData.getCatalogs();
                while (resultSet.next()) {
                    schemaList.add(resultSet.getString("TABLE_CAT"));
                }
            } else {
                resultSet = metaData.getSchemas();
                while (resultSet.next()) {
                    schemaList.add(resultSet.getString("TABLE_SCHEM"));
                }
            }
        } finally {
            Closer.closeQuietly(resultSet);
            Closer.closeQuietly(connection);
        }
        return schemaList;
    }

    /**
     * 获取指定schema下所有表
     *
     *
     * @param connection
     * @param schema
     * @return
     * @throws SQLException
     */
    public static List<String> getTableNamesAutoClose(Connection connection, String schema) throws SQLException {
        return getTableNames(connection, schema, null, true);
    }

    public static List<String> getTableNames(Connection connection, String schema, TableFilter tableFilter) throws SQLException {
       return getTableNames(connection, schema, tableFilter, false);
    }

    public static List<String> getTableNames(Connection connection, String schema, TableFilter tableFilter, boolean closeConnection) throws SQLException {
        ResultSet resultSet = null;
        List<String> tableNameList = new ArrayList<>();
        try {
            resultSet = connection.getMetaData().getTables(schema, schema, "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                // 过滤oracle元数据表
                if (!tableName.startsWith("BIN$") && !tableName.startsWith("RUPD$") && !tableName.startsWith("MLOG$")) {
                    if (tableFilter == null) {
                        tableNameList.add(tableName);
                    } else {
                        tableFilter.doFilter(tableNameList, tableName);
                    }
                }
            }
        } finally {
            Closer.closeQuietly(resultSet);
            if (closeConnection) {
                Closer.closeQuietly(connection);
            }
        }

        return tableNameList;
    }

    public interface TableFilter {
        void doFilter(List<String> tableNameList, String tableName);
    }
}
