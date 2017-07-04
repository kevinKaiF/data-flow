package com.github.dataflow.sender.activemq.handler;

import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.activemq.ActivemqDataSender;
import com.github.dataflow.sender.activemq.config.ActivemqConfig;
import com.github.dataflow.sender.activemq.enums.ActivemqType;
import com.github.dataflow.sender.core.AbstractDataSenderHandler;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.exception.DataSenderException;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/4
 */
public class ActivemqDataSenderHandler extends AbstractDataSenderHandler {
    private DataSourceType dataSourceType = DataSourceType.ACTIVEMQ;

    @Override
    public boolean support(int type) {
        return dataSourceType.getType() == type;
    }

    @Override
    protected DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        Properties properties = parseToProperties(dataOutputMapping.getDataSourceOutput().getOptions());
        String brokeUrl = PropertyUtil.getString(properties, ActivemqConfig.BROKE_URL);
        if (StringUtils.isEmpty(brokeUrl)) {
            throw new DataSenderException("the brokeUrl property of DataOutputMapping.DataSourceOutput must not be null.");
        }

        Properties dataOutputMappingOptions = parseToProperties(dataOutputMapping.getOptions());
        int type = PropertyUtil.getInt(dataOutputMappingOptions, ActivemqConfig.TYPE, ActivemqType.QUEUE.getType());
        if (type == ActivemqType.QUEUE.getType()) {
            String queueName = PropertyUtil.getString(dataOutputMappingOptions, ActivemqConfig.QUEUE);
            if (StringUtils.isEmpty(queueName)) {
                throw new DataSenderException("the queue property of DataOutputMapping must not be null.");
            }
        } else {
            String topic = PropertyUtil.getString(dataOutputMappingOptions, ActivemqConfig.TOPIC);
            if (StringUtils.isEmpty(topic)) {
                throw new DataSenderException("the topic property of DataOutputMapping must not be null.");
            }
        }

        properties.putAll(dataOutputMappingOptions);
        return new ActivemqDataSender(properties);
    }
}
