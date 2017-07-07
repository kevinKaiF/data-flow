package com.github.dataflow.sender.core.handler;


import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.event.EventHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/19
 */
public abstract class AbstractDataSenderHandler implements DataSenderHandler, ApplicationContextAware {
    protected List<EventHandler> eventHandlers = new ArrayList<>();

    @Override
    public DataSender doCreateDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        DataSender dataSender = createDataSender(dataOutputMapping);
        afterCreateDataSender(dataSender, dataOutputMapping);
        return dataSender;
    }

    protected void afterCreateDataSender(DataSender dataSender, DataOutputMapping dataOutputMapping) {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, EventHandler> eventHandlerMap = applicationContext.getBeansOfType(EventHandler.class);
        eventHandlers.clear();
        eventHandlers.addAll(eventHandlerMap.values());
    }

    protected abstract DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception;

    /**
     * 设置DataOutputMapping的options默认值，校验必须属性
     *
     * @param dataOutputMapping
     * @return
     */
    protected abstract Properties refreshDataOutputMapping(DataOutputMapping dataOutputMapping);

    protected Properties parseToProperties(String options) {
        if (StringUtils.isEmpty(options)) {
            return new Properties();
        } else {
            return JSONObject.parseObject(options, Properties.class);
        }
    }


}
