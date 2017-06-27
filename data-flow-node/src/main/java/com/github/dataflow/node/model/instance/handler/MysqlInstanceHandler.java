package com.github.dataflow.node.model.instance.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.github.dataflow.core.instance.Instance;
import com.github.dataflow.core.instance.InstanceType;
import com.github.dataflow.core.instance.handler.AbstractInstanceHandler;
import com.github.dataflow.core.instance.handler.InstanceHandler;
import com.github.dataflow.core.store.DataStore;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.node.model.config.DataFlowContext;
import com.github.dataflow.node.model.instance.MysqlInstance;
import com.github.dataflow.node.model.store.DefaultDataStore;
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

    private InstanceType instanceType = InstanceType.MYSQL;

    public boolean support(int instanceType) {
        return this.instanceType.getType() == instanceType;
    }

    public Instance doCreateInstance(DataInstance dataInstance) {
        MysqlInstance mysqlInstance = new MysqlInstance();
        mysqlInstance.setJdbcUrl(dataInstance.getJdbcUrl());
        mysqlInstance.setName(dataInstance.getName());
        mysqlInstance.setWhiteFilter(dataInstance.getWhiteFilter());
        mysqlInstance.setCanalParameter(buildCanalParameter(dataInstance));
        mysqlInstance.setAlarmService(dataFlowContext.getAlarmService());
        mysqlInstance.setDataStore(buildDataStore(dataInstance));
        mysqlInstance.init();
        return mysqlInstance;
    }

    private CanalParameter buildCanalParameter(DataInstance dataInstance) {
        JSONObject options = JSON.parseObject(dataInstance.getOptions());
        CanalParameter parameter = new CanalParameter();
        parameter.setMetaMode(CanalParameter.MetaMode.ZOOKEEPER);
        parameter.setHaMode(CanalParameter.HAMode.HEARTBEAT);
        parameter.setIndexMode(CanalParameter.IndexMode.ZOOKEEPER);
        parameter.setSourcingType(CanalParameter.SourcingType.MYSQL);
        parameter.setDbAddresses(Collections.singletonList(new InetSocketAddress(dataInstance.getHost(), dataInstance.getPort() != null ? dataInstance.getPort() : 3306)));
        parameter.setDbUsername(dataInstance.getUsername());
        parameter.setDbPassword(dataInstance.getPassword());
//        parameter.setPositions(Arrays.asList("{\"journalName\":\"mysql-bin.000001\",\"position\":6163L,\"timestamp\":1322803601000L}",
//                "{\"journalName\":\"mysql-bin.000001\",\"position\":6163L,\"timestamp\":1322803601000L}"));

        parameter.setSlaveId(dataInstance.getSlaveId());
        parameter.setZkClusters(Arrays.asList(getZookeeperAddresses().split(",")));
        // 忽略表解析错误，比如删除表，删除字段等等
        parameter.setFilterTableError(true);
        parameter.setDefaultConnectionTimeoutInSeconds(30);
        parameter.setConnectionCharset("UTF-8");
        parameter.setConnectionCharsetNumber((byte) 33);
        parameter.setReceiveBufferSize(8 * 1024);
        parameter.setSendBufferSize(8 * 1024);
        //过滤掉库
        parameter.setBlackFilter(dataInstance.getBlackFilter());

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
}
