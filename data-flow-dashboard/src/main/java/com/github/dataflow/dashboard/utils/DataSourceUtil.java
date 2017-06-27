package com.github.dataflow.dashboard.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.dataflow.dubbo.model.DataInstance;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : version
 * @version : Ver 1.0
 * @description :
 * @date : 2017/5/9
 */
public class DataSourceUtil {
    public static javax.sql.DataSource getDataSource(DataInstance dataInstance) {
        // just support mysql
        return getMySqlDataSource(dataInstance);
    }

    public static Connection getConnection(DataInstance dataInstance) throws SQLException {
        return getDataSource(dataInstance).getConnection();
    }

//    private static javax.sql.DataSource getOracleDataSource(DataInstance dataInstance) {
//        OracleDataSource oracleDataSource = null;
//        try {
//            oracleDataSource = new OracleDataSource();
//            oracleDataSource.setURL(dataInstance.getJdbcUrl());
//            oracleDataSource.setUser(dataInstance.getUsername());
//            oracleDataSource.setPassword(dataInstance.getPassword());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return oracleDataSource;
//    }

    private static javax.sql.DataSource getMySqlDataSource(DataInstance dataInstance) {
        try {
            int maxWait = 5 * 1000;
            int minIdle = 0;
            int initialSize = 0;
            int maxActive = 32;
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl(dataInstance.getJdbcUrl());
            druidDataSource.setUsername(dataInstance.getUsername());
            druidDataSource.setPassword(dataInstance.getPassword());
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
            e.printStackTrace();
        }
        return null;
    }
}
