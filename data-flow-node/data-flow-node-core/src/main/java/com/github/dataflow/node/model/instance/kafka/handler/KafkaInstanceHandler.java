package com.github.dataflow.node.model.instance.kafka.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.core.alarm.AlarmService;
import com.github.dataflow.core.instance.Instance;
import com.github.dataflow.core.instance.config.MessageAwareInstanceConfig;
import com.github.dataflow.core.instance.handler.AbstractMessageAwareInstanceHandler;
import com.github.dataflow.core.instance.handler.InstanceHandler;
import com.github.dataflow.core.store.DataStore;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.node.model.config.DataFlowContext;
import com.github.dataflow.node.model.instance.kafka.KafkaInstance;
import com.github.dataflow.node.model.store.DefaultDataStore;
import com.github.dataflow.sender.kafka.config.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/30
 */
@Component
public class KafkaInstanceHandler extends AbstractMessageAwareInstanceHandler implements InstanceHandler {
    private DataSourceType dataSourceType = DataSourceType.KAFKA;

    @Autowired
    private DataFlowContext dataFlowContext;

    @Override
    protected DataStore doBuildDataStore() {
        return new DefaultDataStore();
    }

    @Override
    public boolean support(int instanceType) {
        return dataSourceType.getType() == instanceType;
    }

    @Override
    public Instance createInstance(DataInstance dataInstance) {
        JSONObject options = JSONObjectUtil.parseJSON(dataInstance.getOptions());
        validateProperties(options, ConsumerConfig.GROUP_ID_CONFIG);
        validateProperties(options, ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG);
        validateProperties(options, KafkaConfig.TOPIC);
        // set property
        Long timeout = JSONObjectUtil.getLong(options, MessageAwareInstanceConfig.POLL_TIMEOUT, DEFAULT_TIMEOUT);
        Long period = JSONObjectUtil.getLong(options, MessageAwareInstanceConfig.POLL_PERIOD, DEFAULT_PERIOD);
        options.put(MessageAwareInstanceConfig.POLL_TIMEOUT, timeout);
        options.put(MessageAwareInstanceConfig.POLL_PERIOD, period);
        options.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        options.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // create instance
        return new KafkaInstance(options);
    }

    @Override
    protected AlarmService getAlarmService() {
        return dataFlowContext.getAlarmService();
    }
}
