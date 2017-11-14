package com.github.dataflow.sender.database.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.sender.database.OracleDataSender;
import com.github.dataflow.sender.database.config.DatabaseConfig;
import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/24
 */
public class OracleDataSenderHandler extends AbstractDatabaseDataSenderHandler {
    @Override
    protected DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        OracleDataSender dataSender = new OracleDataSender();
        JSONObject props = JSONObjectUtil.parseJSON(dataOutputMapping.getDataSourceOutput().getOptions());
        dataSender.setDataSourceHolder(new DataSourceHolder(getDataSource(props)));
        return dataSender;
    }

    protected DataSource getDataSource(JSONObject props) {
        OracleDataSource oracleDataSource = null;
        try {
            oracleDataSource = new OracleDataSource();
            oracleDataSource.setURL(JSONObjectUtil.getString(props, DatabaseConfig.JDBC_URL));
            oracleDataSource.setUser(JSONObjectUtil.getString(props, DatabaseConfig.USERNAME));
            oracleDataSource.setPassword(JSONObjectUtil.getString(props, DatabaseConfig.PASSWORD));
            return oracleDataSource;
        } catch (SQLException e) {
            throw new DataSenderException(e);
        }
    }
}
