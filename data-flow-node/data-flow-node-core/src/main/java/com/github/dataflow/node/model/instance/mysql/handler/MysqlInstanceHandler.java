package com.github.dataflow.node.model.instance.mysql.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.github.dataflow.core.alarm.AlarmService;
import com.github.dataflow.core.instance.Instance;
import com.github.dataflow.core.instance.handler.AbstractInstanceHandler;
import com.github.dataflow.core.instance.handler.InstanceHandler;
import com.github.dataflow.core.store.DataStore;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.node.model.config.DataFlowContext;
import com.github.dataflow.node.model.instance.mysql.MysqlInstance;
import com.github.dataflow.node.model.instance.mysql.config.MysqlInstanceConfig;
import com.github.dataflow.node.model.store.DefaultDataStore;
import com.github.dataflow.sender.database.config.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author kevin
 * @date 2017-05-28 3:55 PM.
 */
@Component
public class MysqlInstanceHandler extends AbstractInstanceHandler implements InstanceHandler {
    @Autowired
    private DataFlowContext dataFlowContext;

    private DataSourceType instanceType = DataSourceType.MYSQL;

    public boolean support(int instanceType) {
        return this.instanceType.getType() == instanceType;
    }

    public Instance createInstance(DataInstance dataInstance) {
        JSONObject options = parseToProperties(dataInstance.getOptions());
        MysqlInstance mysqlInstance = new MysqlInstance();
        mysqlInstance.setWhiteFilter(options.getString(MysqlInstanceConfig.WHITE_FILTER));
        mysqlInstance.setCanalParameter(buildCanalParameter(options));
        return mysqlInstance;
    }

    private CanalParameter buildCanalParameter(JSONObject options) {
        CanalParameter parameter = new CanalParameter();
        parameter.setMetaMode(CanalParameter.MetaMode.ZOOKEEPER);
        parameter.setHaMode(CanalParameter.HAMode.HEARTBEAT);
        parameter.setIndexMode(CanalParameter.IndexMode.ZOOKEEPER);
        parameter.setSourcingType(CanalParameter.SourcingType.MYSQL);
        parameter.setDbAddresses(Collections.singletonList(new InetSocketAddress(options.getString(DatabaseConfig.HOST), options.getInteger(DatabaseConfig.PORT) == null ? 3306 : options.getInteger(DatabaseConfig.PORT) )));
        parameter.setDbUsername(options.getString(DatabaseConfig.USERNAME));
        parameter.setDbPassword(options.getString(DatabaseConfig.PASSWORD));
//        parameter.setPositions(Arrays.asList("{\"journalName\":\"mysql-bin.000001\",\"position\":6163L,\"timestamp\":1322803601000L}",
//                "{\"journalName\":\"mysql-bin.000001\",\"position\":6163L,\"timestamp\":1322803601000L}"));

        parameter.setSlaveId(options.getLong("slaveId"));
        parameter.setZkClusters(Arrays.asList(getZookeeperAddresses().split(",")));
        // 忽略表解析错误，比如删除表，删除字段等等
        parameter.setFilterTableError(true);
        parameter.setDefaultConnectionTimeoutInSeconds(30);
        parameter.setConnectionCharset("UTF-8");
        parameter.setConnectionCharsetNumber((byte) 33);
        parameter.setReceiveBufferSize(8 * 1024);
        parameter.setSendBufferSize(8 * 1024);
        //过滤掉库
        parameter.setBlackFilter(options.getString(MysqlInstanceConfig.BLACK_FILTER));

        parameter.setDetectingEnable(false);
        parameter.setDetectingIntervalInSeconds(10);
        parameter.setDetectingRetryTimes(3);
        parameter.setDetectingSQL("SELECT 'x' FROM DUAL");

        return parameter;
    }

    @Override
    protected DataStore doBuildDataStore() {
        return new DefaultDataStore();
    }

    @Override
    protected AlarmService getAlarmService() {
        return dataFlowContext.getAlarmService();
    }
}
