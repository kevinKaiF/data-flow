package com.github.dataflow.node.model.instance.oracle;

import com.github.dataflow.common.utils.Closer;
import com.github.dataflow.common.utils.DBMetaUtil;
import com.github.dataflow.node.exception.InstanceException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/11/9
 */
public class TableNameParser {
    public static List<String> parse(DataSource oracleDataSource, String whiteFilter, String blackFilter) {
        Connection connection = null;
        List<String> tableNamesToUse = new ArrayList<>();
        try {
            Set<String> tableNames = new HashSet<>();
            connection = oracleDataSource.getConnection();
            final Map<String, Map<String, String>> blackTableMap = getBlackTableMap(blackFilter, connection);
            String[] splits = whiteFilter.split(",");
            DatabaseMetaData metaData = connection.getMetaData();
            for (String split : splits) {
                String fullTableName = split.trim();
                if (!StringUtils.isEmpty(fullTableName)) {
                    String[] str = fullTableName.split("\\.");
                    final String schemaName = DBMetaUtil.getCanonicalName(str[0], metaData);
                    String tableName = DBMetaUtil.getCanonicalName(str[1], metaData);
                    // schema.*这种格式
                    if ("*".equals(tableName)) {
                        List<String> tableNameList = DBMetaUtil.getTableNames(connection, schemaName, new DBMetaUtil.TableFilter() {
                            @Override
                            public void doFilter(List<String> tableNameList, String tableName) {
                                String fullTableName = schemaName + "." + tableName;
                                if (!filterBlackTable(schemaName, tableName, blackTableMap)) {
                                    tableNameList.add(fullTableName);
                                }
                            }
                        });
                        tableNames.addAll(tableNameList);
                    } else {
                        if (!filterBlackTable(schemaName, tableName, blackTableMap)) {
                            tableNames.add(fullTableName);
                        }
                    }
                }
            }

            tableNamesToUse.addAll(tableNames);
        } catch (SQLException e) {
            throw new InstanceException(e);
        } finally {
            Closer.closeQuietly(connection);
        }

        if (tableNamesToUse.size() == 0) {
            throw new InstanceException(String.format("there is no valid tables, whiteFilter : %s, blackFilter : %s", whiteFilter, blackFilter));
        }
        return tableNamesToUse;
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

    private static  Map<String, Map<String, String>> getBlackTableMap(String blackFilter, Connection connection) throws SQLException {
        Map<String, Map<String, String>> blackTableMap = new HashMap<>();
        DatabaseMetaData metaData = connection.getMetaData();
        if (!StringUtils.isEmpty(blackFilter)) {
            String[] schemaAndTable = blackFilter.split(",");
            for (String split : schemaAndTable) {
                String fullTableName = split.trim();
                if (!StringUtils.isEmpty(fullTableName)) {
                    String[] str = fullTableName.split("\\.");
                    String schemaName = DBMetaUtil.getCanonicalName(str[0], metaData);
                    String tableName = DBMetaUtil.getCanonicalName(str[1], metaData);
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
}
