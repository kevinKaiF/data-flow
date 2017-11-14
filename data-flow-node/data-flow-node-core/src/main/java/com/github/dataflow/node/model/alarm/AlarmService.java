package com.github.dataflow.node.model.alarm;


import com.github.dataflow.common.model.DataFlowLifeCycle;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/4
 */
public interface AlarmService extends DataFlowLifeCycle {
    /**
     * 发送异常信息，默认会持久化到数据库
     *
     * @param instanceName
     * @param message
     */
    void sendAlarm(String instanceName, String message);

    /**
     * 发送异常信息
     * <p>
     * note ：当store为true会持久化到数据库，否则不会
     *
     * @param instanceName
     * @param message
     * @param store
     */
    void sendAlarm(String instanceName, String message, boolean store);
}
