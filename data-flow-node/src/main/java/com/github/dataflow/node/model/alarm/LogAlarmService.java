package com.github.dataflow.node.model.alarm;

import com.github.dataflow.core.alarm.AlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/6
 */
public class LogAlarmService extends AbstractAlarmService implements AlarmService {
    private static final Logger logger = LoggerFactory.getLogger(LogAlarmService.class);

    @Override
    protected void doSendAlarm(String instanceName, String message) {
        logger.info("{} - {}", instanceName, message);
    }
}
