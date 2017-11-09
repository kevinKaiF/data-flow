package com.github.dataflow.node.model.instance.oracle.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.Closer;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.core.alarm.AlarmService;
import com.github.dataflow.core.exception.InstanceException;
import com.github.dataflow.core.instance.Instance;
import com.github.dataflow.core.instance.handler.AbstractInstanceHandler;
import com.github.dataflow.core.instance.handler.InstanceHandler;
import com.github.dataflow.core.store.DataStore;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.node.model.config.DataFlowContext;
import com.github.dataflow.node.model.instance.oracle.OracleInstance;
import com.github.dataflow.node.model.instance.oracle.config.OracleInstanceConfig;
import com.github.dataflow.node.model.store.DefaultDataStore;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/11/8
 */
public class OracleInstanceHandler extends AbstractInstanceHandler implements InstanceHandler {
    @Autowired
    private DataFlowContext dataFlowContext;

    private DataSourceType dataSourceType = DataSourceType.ORACLE;

    @Override
    public boolean support(int instanceType) {
        return this.dataSourceType.getType() == instanceType;
    }

    @Override
    protected DataStore doBuildDataStore() {
        return new DefaultDataStore();
    }

    @Override
    protected AlarmService getAlarmService() {
        return dataFlowContext.getAlarmService();
    }

    @Override
    protected Instance createInstance(DataInstance dataInstance) {
        JSONObject options = JSON.parseObject(dataInstance.getOptions());
        validateProperties(options, OracleInstanceConfig.WHITE_FILTER);
        String whiteFilter = JSONObjectUtil.getString(options, OracleInstanceConfig.WHITE_FILTER);
        String blackFilter = JSONObjectUtil.getString(options, OracleInstanceConfig.BLACK_FILTER);
        validateTableFilter(whiteFilter, OracleInstanceConfig.WHITE_FILTER);
        validateTableFilter(blackFilter, OracleInstanceConfig.BLACK_FILTER);
        OracleInstance oracleInstance = new OracleInstance(options, getOracleDataSource(options));
        return oracleInstance;
    }

    private void validateTableFilter(String tableFilter, String propertyName) {
        if (!StringUtils.isEmpty(tableFilter)) {
            String[] split = tableFilter.split(",");
            for (String string : split) {
                String fullTableName = string.trim();
                if (!StringUtils.isEmpty(fullTableName)) {
                    if (!fullTableName.contains(".")) {
                        throw new InstanceException(String.format("%s[%s] is invalid, schemaName and tableName must separated by '.'", propertyName, tableFilter));
                    }

                    if (fullTableName.startsWith("*")) {
                        throw new InstanceException(String.format("%s[%s] is invalid, schemaName must not start with '*'", propertyName, tableFilter));
                    }
                }
            }
        }
    }


    private javax.sql.DataSource getOracleDataSource(JSONObject options) {
        OracleDataSource oracleDataSource = null;
        Connection connection = null;
        try {
            JSONObject properties = options;
            oracleDataSource = new OracleDataSource();
            oracleDataSource.setURL(JSONObjectUtil.getString(properties, OracleInstanceConfig.JDBC_URL));
            oracleDataSource.setUser(JSONObjectUtil.getString(properties, OracleInstanceConfig.USERNAME));
            oracleDataSource.setPassword(JSONObjectUtil.getString(properties, OracleInstanceConfig.PASSWORD));
            connection = oracleDataSource.getConnection();
        } catch (SQLException e) {
            throw new InstanceException(e);
        } finally {
            Closer.closeQuietly(connection);
        }
        return oracleDataSource;
    }

}
