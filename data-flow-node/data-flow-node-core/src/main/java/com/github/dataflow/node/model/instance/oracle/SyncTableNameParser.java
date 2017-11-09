package com.github.dataflow.node.model.instance.oracle;

import com.github.dataflow.common.utils.Closer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/11/9
 */
public class SyncTableNameParser {
    public static List<String> parse(DataSource oracleDataSource, String whiteFilter, String blackFilter) {
        Connection connection = null;
        try {
            Set<String> tableNames = new HashSet<>();
            Map<String, Map<String, String>> blackTableMap = getBlackTableMap(blackFilter);
            String[] splits = whiteFilter.split(",");
            connection = oracleDataSource.getConnection();
            for (String split : splits) {
                String fullTableName = split.trim();
                if (!StringUtils.isEmpty(fullTableName)) {
                    String[] str = fullTableName.split("\\.");
                    String schemaName = str[0].toUpperCase();
                    String tableName = str[1].toUpperCase();
                    if ("*".equals(tableName)) {
                        tableNames.addAll(getTableNames(connection, schemaName, blackTableMap));
                    } else {
                        if (!filterBlackTable(schemaName, tableName, blackTableMap)) {
                            tableNames.add(fullTableName);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Closer.closeQuietly(connection);
        }
        return null;
    }

    /**
     * 过滤黑名单的表
     *
     * @param schemaName
     * @param tableName
     * @param blackTableMap
     * @return
     */
    private static boolean filterBlackTable(String schemaName, String tableName, Map<String, Map<String, String>> blackTableMap) {
        if (!CollectionUtils.isEmpty(blackTableMap)) {
            Map<String, String> tableMap = blackTableMap.get(schemaName);
            if (CollectionUtils.isEmpty(tableMap)) {
                return false;
            } else {
                String any = tableMap.get("*");
                // 表是否是任意的
                if (any == null) {
                    String tableNameToUse = tableMap.get(tableName);
                    if (tableNameToUse == null) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    private static Map<String, Map<String, String>> getBlackTableMap(String blackFilter) {
        Map<String, Map<String, String>> blackTableMap = new HashMap<>();
        if (!StringUtils.isEmpty(blackFilter)) {
            String[] ss = blackFilter.split(",");
            for (String split : ss) {
                String fullTableName = split.trim();
                if (!StringUtils.isEmpty(fullTableName)) {
                    String[] str = fullTableName.split("\\.");
                    String schemaName = str[0].toUpperCase();
                    String tableName = str[1].toUpperCase();
                    Map<String, String> tableMap = blackTableMap.get(schemaName);
                    if (tableMap == null) {
                        tableMap = new HashMap<>();
                        blackTableMap.put(schemaName, tableMap);
                    }

                    tableMap.put(tableName, tableName);
                }
            }
        }
        return blackTableMap;
    }

    private static List<String> getTableNames(Connection connection, String schemaName, Map<String, Map<String, String>> blackTableMap) throws SQLException {
        ResultSet resultSet = null;
        List<String> tableNameList = new ArrayList<>();
        try {
            resultSet = connection.getMetaData().getTables(schemaName, null, "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String fullTableName = schemaName + "." + tableName;
                if (!filterBlackTable(schemaName, tableName, blackTableMap)) {
                    tableNameList.add(fullTableName);
                }
            }
        } finally {
            Closer.closeQuietly(resultSet);
        }
        return tableNameList;
    }
}
