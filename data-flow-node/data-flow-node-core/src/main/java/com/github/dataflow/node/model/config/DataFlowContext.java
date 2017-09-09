package com.github.dataflow.node.model.config;

import com.github.dataflow.core.alarm.AlarmService;
import com.github.dataflow.core.alarm.handler.AlarmServiceHandler;
import com.github.dataflow.dubbo.model.DataAlarm;
import com.github.dataflow.dubbo.model.DataNodeConfiguration;
import com.github.dataflow.node.model.alarm.LogAlarmService;
import com.github.dataflow.node.service.DataLogService;
import com.github.dataflow.node.service.DataNodeConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/26
 */
@Component
public class DataFlowContext implements ApplicationContextAware, InitializingBean {
    private Logger logger = LoggerFactory.getLogger(DataFlowContext.class);

    private DataNodeConfiguration dataNodeConfiguration;

    @Autowired
    private DataNodeConfigurationService dataNodeConfigurationService;

    @Autowired
    private GlobalExecutor globalExecutor;

    @Autowired
    private DataLogService dataLogService;

    private AlarmService alarmService;

    private ApplicationContext applicationContext;


    @Override
    public void afterPropertiesSet() throws Exception {
        DataNodeConfiguration dataNodeConfiguration = dataNodeConfigurationService.getDataNodeConfiguration();
        if (dataNodeConfiguration == null) {
            logger.warn("there is no any DataNodeConfiguration.");
        } else {
            this.dataNodeConfiguration = dataNodeConfiguration;
            Map<String, AlarmServiceHandler> alarmServiceHandlerMap = applicationContext.getBeansOfType(AlarmServiceHandler.class);
            alarmService = createAlarmService(alarmServiceHandlerMap);
        }

        logger.warn("AlarmService is not found, use com.github.dataflow.node.model.alarm.LogAlarmService instead.");
        if (alarmService == null) {
            alarmService = new LogAlarmService();
        }
    }

    private AlarmService createAlarmService(Map<String, AlarmServiceHandler> alarmServiceHandlerMap) throws Exception {
        DataAlarm dataAlarm = new DataAlarm();
        dataAlarm.setType(dataNodeConfiguration.getType());
        dataAlarm.setOptions(dataNodeConfiguration.getOptions());

        for (AlarmServiceHandler alarmServiceHandler : alarmServiceHandlerMap.values()) {
            if (alarmServiceHandler.support(dataAlarm.getType())) {
                return alarmService = alarmServiceHandler.doCreateAlarmService(dataAlarm);
            }
        }

        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public AlarmService getAlarmService() {
        return alarmService;
    }

    public GlobalExecutor getGlobalExecutor() {
        return globalExecutor;
    }

    public DataNodeConfiguration getDataNodeConfiguration() {
        return dataNodeConfiguration;
    }

    public DataLogService getDataLogService() {
        return dataLogService;
    }
}
