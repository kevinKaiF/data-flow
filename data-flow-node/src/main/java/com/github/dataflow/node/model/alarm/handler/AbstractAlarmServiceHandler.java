package com.github.dataflow.node.model.alarm.handler;

import com.github.dataflow.core.alarm.AlarmService;
import com.github.dataflow.core.alarm.handler.AlarmServiceHandler;
import com.github.dataflow.dubbo.model.DataAlarm;
import com.github.dataflow.node.model.alarm.AbstractAlarmService;
import com.github.dataflow.node.model.config.DataFlowContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/26
 */
public abstract class AbstractAlarmServiceHandler implements AlarmServiceHandler {
    @Autowired
    protected DataFlowContext dataFlowContext;

    @Override
    public AlarmService doCreateAlarmService(DataAlarm dataAlarm) throws Exception {
        AlarmService alarmService = createAlarmService(dataAlarm);
        if (alarmService instanceof AbstractAlarmService) {
            AbstractAlarmService abstractAlarmService = (AbstractAlarmService) alarmService;
            abstractAlarmService.setDataLogService(dataFlowContext.getDataLogService());
            abstractAlarmService.setExecutorService(dataFlowContext.getGlobalExecutor());
        }

        return alarmService;
    }

    protected abstract AlarmService createAlarmService(DataAlarm dataAlarm);
}
