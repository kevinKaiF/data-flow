package com.github.dataflow.sender.database.handler;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.sender.database.MysqlDataSender;
import com.github.dataflow.sender.database.config.DatabaseConfig;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/24
 */
public class MysqlDataSenderHandler extends AbstractDatabaseDataSenderHandler {

    @Override
    protected DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        MysqlDataSender dataSender = new MysqlDataSender();
        JSONObject props = JSONObjectUtil.parseJSON(dataOutputMapping.getDataSourceOutput().getOptions());
        dataSender.setDataSourceHolder(new DataSourceHolder(getDataSource(props)));
        return dataSender;
    }

    protected javax.sql.DataSource getDataSource(JSONObject props) {
        try {
            int maxWait = 5000;
            int minIdle = 0;
            int initialSize = 0;
            int maxActive = 32;
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl(JSONObjectUtil.getString(props, DatabaseConfig.JDBC_URL));
            druidDataSource.setUsername(JSONObjectUtil.getString(props, DatabaseConfig.USERNAME));
            druidDataSource.setPassword(JSONObjectUtil.getString(props, DatabaseConfig.PASSWORD));
            druidDataSource.setUseUnfairLock(true);
            druidDataSource.setNotFullTimeoutRetryCount(2);
            druidDataSource.setInitialSize(JSONObjectUtil.getInt(props, "initialSize", initialSize));
            druidDataSource.setMinIdle(JSONObjectUtil.getInt(props, "minIdle", minIdle));
            druidDataSource.setMaxActive(JSONObjectUtil.getInt(props, "maxActive", maxActive));
            druidDataSource.setMaxWait(JSONObjectUtil.getInt(props, "maxWait", maxWait));
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
            throw new DataSenderException(e);
        }
    }

}
