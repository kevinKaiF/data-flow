package com.github.dataflow.node.model.instance.activemq.handler;

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
import com.github.dataflow.node.model.instance.activemq.ActivemqInstance;
import com.github.dataflow.node.model.store.DefaultDataStore;
import com.github.dataflow.sender.activemq.config.ActivemqConfig;
import com.github.dataflow.sender.activemq.enums.ActivemqType;
import org.apache.activemq.ActiveMQConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
@Component
public class ActivemqInstanceHandler extends AbstractMessageAwareInstanceHandler implements InstanceHandler {
    @Autowired
    private DataFlowContext dataFlowContext;

    private DataSourceType dataSourceType = DataSourceType.ACTIVEMQ;

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
        JSONObject options = parseToProperties(dataInstance.getOptions());
        validateProperties(options, ActivemqConfig.BROKE_URL);
        validateProperties(options, ActivemqConfig.TYPE);
        int type = JSONObjectUtil.getInt(options, ActivemqConfig.TYPE);
        if (type == ActivemqType.QUEUE.getType()) {
            validateProperties(options, ActivemqConfig.QUEUE);
        } else {
            validateProperties(options, ActivemqConfig.TOPIC);
        }
        // set property
        String username = JSONObjectUtil.getString(options, ActivemqConfig.USERNAME, ActiveMQConnection.DEFAULT_USER);
        String password = JSONObjectUtil.getString(options, ActivemqConfig.PASSWORD, ActiveMQConnection.DEFAULT_PASSWORD);
        Long timeout = JSONObjectUtil.getLong(options, MessageAwareInstanceConfig.POLL_TIMEOUT, DEFAULT_TIMEOUT);
        Long period = JSONObjectUtil.getLong(options, MessageAwareInstanceConfig.POLL_PERIOD, DEFAULT_PERIOD);
        options.put(ActivemqConfig.USERNAME, username);
        options.put(ActivemqConfig.PASSWORD, password);
        options.put(MessageAwareInstanceConfig.POLL_TIMEOUT, timeout);
        options.put(MessageAwareInstanceConfig.POLL_PERIOD, period);
        // create instance
        return new ActivemqInstance(options);
    }

    @Override
    protected AlarmService getAlarmService() {
        return dataFlowContext.getAlarmService();
    }
}
