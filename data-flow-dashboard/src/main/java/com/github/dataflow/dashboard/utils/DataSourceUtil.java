package com.github.dataflow.dashboard.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.dashboard.exception.DataFlowException;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataInstance;
import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/5/9
 */
public class DataSourceUtil {
    private static Map<DataInstance, DataSource> dataSourceCache = new ConcurrentHashMap<>();

    private static javax.sql.DataSource getDataSource(DataInstance dataInstance) {
        DataSourceType dataSourceType = DataSourceType.parse(dataInstance.getType());
        switch (dataSourceType) {
            case MYSQL:
                return getMySqlDataSource(dataInstance);
            case ORACLE:
                return getOracleDataSource(dataInstance);
            default:
                throw new DataFlowException("don't support DataSourceType : " + dataSourceType);
        }
    }

    public static synchronized Connection getConnection(DataInstance dataInstance) throws SQLException {
        DataSource dataSource = dataSourceCache.get(dataInstance);
        if (dataSource == null) {
            dataSource = getDataSource(dataInstance);
            dataSourceCache.put(dataInstance, dataSource);
        }
        return dataSource.getConnection();
    }

    private static javax.sql.DataSource getOracleDataSource(DataInstance dataInstance) {
        try {
            Properties properties = JSON.parseObject(dataInstance.getOptions(), Properties.class);
            OracleDataSource oracleDataSource = new OracleDataSource();
            oracleDataSource.setURL(PropertyUtil.getString(properties, "jdbcUrl"));
            oracleDataSource.setUser(PropertyUtil.getString(properties, "username"));
            oracleDataSource.setPassword(PropertyUtil.getString(properties, "password"));
            return oracleDataSource;
        } catch (SQLException e) {
            throw new DataFlowException(e);
        }
    }

    private static javax.sql.DataSource getMySqlDataSource(DataInstance dataInstance) {
        try {
            int maxWait = 5 * 1000;
            int minIdle = 0;
            int initialSize = 0;
            int maxActive = 32;
            Properties properties = JSON.parseObject(dataInstance.getOptions(), Properties.class);
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl(PropertyUtil.getString(properties, "jdbcUrl"));
            druidDataSource.setUsername(PropertyUtil.getString(properties, "username"));
            druidDataSource.setPassword(PropertyUtil.getString(properties, "password"));
            druidDataSource.setUseUnfairLock(true);
            druidDataSource.setNotFullTimeoutRetryCount(2);
            druidDataSource.setInitialSize(initialSize);
            druidDataSource.setMinIdle(minIdle);
            druidDataSource.setMaxActive(maxActive);
            druidDataSource.setMaxWait(maxWait);
            druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
            druidDataSource.addConnectionProperty("useServerPrepStmts", "false");
            druidDataSource.addConnectionProperty("rewriteBatchedStatements", "true");
            druidDataSource.addConnectionProperty("allowMultiQueries", "true");
            druidDataSource.addConnectionProperty("readOnlyPropagatesToServer", "false");
            druidDataSource.setValidationQuery("select 1");
            druidDataSource.setExceptionSorter("com.alibaba.druid.pool.vendor.MySqlExceptionSorter");
            druidDataSource.setValidConnectionCheckerClassName("com.alibaba.druid.pool.vendor.MySqlValidConnectionChecker");
            return druidDataSource;
        } catch (Exception e) {
            throw new DataFlowException(e);
        }
    }
}
