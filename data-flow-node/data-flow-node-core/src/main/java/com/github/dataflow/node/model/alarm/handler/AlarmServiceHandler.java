package com.github.dataflow.node.model.alarm.handler;


import com.github.dataflow.dubbo.model.DataAlarm;
import com.github.dataflow.node.model.alarm.AlarmService;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/5
 */
public interface AlarmServiceHandler {

    boolean support(int type);

    AlarmService doCreateAlarmService(DataAlarm dataAlarm) throws Exception;
}
