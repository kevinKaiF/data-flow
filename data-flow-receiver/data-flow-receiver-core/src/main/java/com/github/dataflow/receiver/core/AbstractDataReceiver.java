package com.github.dataflow.receiver.core;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.model.AbstractDataFlowLifeCycle;
import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.dubbo.model.DataSourceOutput;
import com.github.dataflow.receiver.core.exception.ReceiverException;
import com.github.dataflow.receiver.core.utils.DataReceiverConfig;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.DataSenderHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/24
 */
public class AbstractDataReceiver extends AbstractDataFlowLifeCycle implements ApplicationContextAware, DataReceiver {
    protected DataSender dataSender;

    protected Properties prop;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, DataSenderHandler> dataSenderHandlerMap = applicationContext.getBeansOfType(DataSenderHandler.class);
        if (CollectionUtils.isEmpty(dataSenderHandlerMap)) {
            throw new ReceiverException("there is no DataSenderHandler, please registry one at least.");
        }

        try {
            prop = loadProperty();
            int dataSourceOutputType = getType();
            DataOutputMapping dataOutputMapping = parseDataOutputMapping(prop);
            for (DataSenderHandler dataSenderHandler : dataSenderHandlerMap.values()) {
                if (dataSenderHandler.support(dataSourceOutputType)) {
                    dataSender = dataSenderHandler.doCreateDataSender(dataOutputMapping);
                }
            }
        } catch (Exception e) {
            throw new ReceiverException(e);
        }

    }

    private DataOutputMapping parseDataOutputMapping(Properties prop) {
        DataSourceOutput dataSourceOutput = new DataSourceOutput();
        dataSourceOutput.setType(getType());
        dataSourceOutput.setOptions(JSONObject.toJSONString(prop));

        DataOutputMapping dataOutputMapping = new DataOutputMapping();
        dataOutputMapping.setDataSourceOutput(dataSourceOutput);
        dataOutputMapping.setTopic(PropertyUtil.getString(prop, DataReceiverConfig.DATA_RECEIVER_TOPIC));
        return dataOutputMapping;
    }

    private int getType() {
        String property = prop.getProperty(DataReceiverConfig.DATA_RECEIVER_TYPE);
        return DataSourceType.parseName(property);
    }

    protected Properties loadProperty() throws IOException {
        return PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
    }
}
