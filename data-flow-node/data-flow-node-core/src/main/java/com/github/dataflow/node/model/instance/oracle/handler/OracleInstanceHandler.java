package com.github.dataflow.node.model.instance.oracle.handler;

import com.github.dataflow.core.alarm.AlarmService;
import com.github.dataflow.core.instance.Instance;
import com.github.dataflow.core.instance.handler.AbstractInstanceHandler;
import com.github.dataflow.core.instance.handler.InstanceHandler;
import com.github.dataflow.core.store.DataStore;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.node.model.config.DataFlowContext;
import com.github.dataflow.node.model.store.DefaultDataStore;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/9/10
 */
public class OracleInstanceHandler extends AbstractInstanceHandler implements InstanceHandler {
    private DataSourceType dataSourceType = DataSourceType.ORACLE;

    @Autowired
    private DataFlowContext dataFlowContext;

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
        return null;
    }

    @Override
    public boolean support(int instanceType) {
        return dataSourceType.getType() == instanceType;
    }
}
