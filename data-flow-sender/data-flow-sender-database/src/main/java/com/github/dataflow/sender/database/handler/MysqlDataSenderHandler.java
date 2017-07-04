package com.github.dataflow.sender.database.handler;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.database.MysqlDataSender;
import com.github.dataflow.sender.database.config.DatabaseConfig;

import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/24
 */
public class MysqlDataSenderHandler extends DatabaseDataSenderHandler {

    @Override
    protected DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        MysqlDataSender dataSender = new MysqlDataSender();
        Properties props = parseToProperties(dataOutputMapping.getDataSourceOutput().getOptions());
        dataSender.setDataSourceHolder(new DataSourceHolder(getMySqlDataSource(props)));
        return dataSender;
    }

    private javax.sql.DataSource getMySqlDataSource(Properties props) {
        try {
            String maxWait = "5000";
            String minIdle = "0";
            String initialSize = "0";
            String maxActive = "32";
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl(PropertyUtil.getString(props, DatabaseConfig.JDBC_URL));
            druidDataSource.setUsername(PropertyUtil.getString(props, DatabaseConfig.USERNAME));
            druidDataSource.setPassword(PropertyUtil.getString(props, DatabaseConfig.PASSWORD));
            druidDataSource.setUseUnfairLock(true);
            druidDataSource.setNotFullTimeoutRetryCount(2);
            druidDataSource.setInitialSize(PropertyUtil.getInt(props, "initialSize", initialSize));
            druidDataSource.setMinIdle(PropertyUtil.getInt(props, "minIdle", minIdle));
            druidDataSource.setMaxActive(PropertyUtil.getInt(props, "maxActive", maxActive));
            druidDataSource.setMaxWait(PropertyUtil.getInt(props, "maxWait", maxWait));
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
