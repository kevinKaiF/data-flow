package com.github.dataflow.node.model.alarm;

import com.alibaba.otter.canal.common.alarm.CanalAlarmHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 针对Canal的AlarmService的delegate
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/4
 */
public class AlarmServiceDelegate extends AbstractAlarmService implements CanalAlarmHandler, AlarmService {
    private static final Logger logger = LoggerFactory.getLogger(AlarmServiceDelegate.class);

    private AlarmService alarmService;

    @Override
    public void doStart() {
        alarmService.start();
    }

    @Override
    protected void doStop() {
        alarmService.stop();
    }

    @Override
    public void doSendAlarm(final String instanceName, final String message) {
        logger.error("Instance:{}[{}]", new Object[]{instanceName, message});
        alarmService.sendAlarm(instanceName, message);
    }

    public void setAlarmService(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

}
