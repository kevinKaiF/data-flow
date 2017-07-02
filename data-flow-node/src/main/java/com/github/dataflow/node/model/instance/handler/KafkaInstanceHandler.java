package com.github.dataflow.node.model.instance.handler;

import com.alibaba.fastjson.JSON;
import com.github.dataflow.core.instance.Instance;
import com.github.dataflow.core.instance.handler.AbstractInstanceHandler;
import com.github.dataflow.core.instance.handler.InstanceHandler;
import com.github.dataflow.core.store.DataStore;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.node.model.config.DataFlowContext;
import com.github.dataflow.node.model.instance.kafka.KafkaInstance;
import com.github.dataflow.node.model.store.DefaultDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/30
 */
@Component
public class KafkaInstanceHandler extends AbstractInstanceHandler implements InstanceHandler {
    @Autowired
    private DataFlowContext dataFlowContext;

    private DataSourceType dataSourceType = DataSourceType.KAFKA;
    @Override
    protected DataStore doBuildDataStore() {
        return new DefaultDataStore();
    }

    @Override
    public boolean support(int instanceType) {
        return dataSourceType.getType() == instanceType;
    }

    @Override
    public Instance doCreateInstance(DataInstance dataInstance) {
        Properties options = JSON.parseObject(dataInstance.getOptions(), Properties.class);
        KafkaInstance kafkaInstance = new KafkaInstance();
        kafkaInstance.setId(dataInstance.getId());
        kafkaInstance.setName(dataInstance.getName());
        kafkaInstance.setDataStore(buildDataStore(dataInstance));
        kafkaInstance.setAlarmService(dataFlowContext.getAlarmService());
        kafkaInstance.setOptions(options);
        return kafkaInstance;
    }
}
