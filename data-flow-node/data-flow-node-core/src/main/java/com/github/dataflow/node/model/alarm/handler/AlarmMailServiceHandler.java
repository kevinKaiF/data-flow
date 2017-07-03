package com.github.dataflow.node.model.alarm.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.core.alarm.AlarmService;
import com.github.dataflow.dubbo.common.enums.DataAlarmType;
import com.github.dataflow.dubbo.model.DataAlarm;
import com.github.dataflow.node.model.alarm.MailAlarmService;
import org.springframework.stereotype.Component;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/5
 */
@Component
public class AlarmMailServiceHandler extends AbstractAlarmServiceHandler {
    private DataAlarmType dataAlarmType = DataAlarmType.MAIL;

    @Override
    public boolean support(int type) {
        return dataAlarmType.getType() == type;
    }

    @Override
    public AlarmService createAlarmService(DataAlarm dataAlarm) {
        JSONObject options = JSON.parseObject(dataAlarm.getOptions());
        MailAlarmService alarmMailService = new MailAlarmService();
        alarmMailService.setEmailHost(options.getString("emailHost"));
        alarmMailService.setEmailUsername(options.getString("emailUsername"));
        alarmMailService.setEmailPassword(options.getString("emailPassword"));
        alarmMailService.setEmailReceiver(options.getString("emailReceiver"));
        return alarmMailService;
    }
}
