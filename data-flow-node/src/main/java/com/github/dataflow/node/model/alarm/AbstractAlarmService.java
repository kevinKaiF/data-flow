package com.github.dataflow.node.model.alarm;

import com.github.dataflow.common.model.AbstractDataFlowLifeCycle;
import com.github.dataflow.core.alarm.AlarmService;
import com.github.dataflow.node.model.config.GlobalExecutor;
import com.github.dataflow.node.service.DataLogService;


/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/25
 */
public abstract class AbstractAlarmService extends AbstractDataFlowLifeCycle implements AlarmService {
    protected GlobalExecutor executorService;

    protected DataLogService dataLogService;

    @Override
    public void start() {
        if (!isStart()) {
            super.start();
            doStart();
        }
    }

    protected abstract void doStart();

    @Override
    public void stop() {
        if (isStart()) {
            doStop();
            super.stop();
        }
    }

    protected abstract void doStop();

    @Override
    public void sendAlarm(final String instanceName, final String message) {
       sendAlarm(instanceName, message, true);
    }

    @Override
    public void sendAlarm(final String instanceName, final String message, boolean store) {
        if (store) {
            if (executorService != null) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        storeAlarm(instanceName, message);
                    }
                });
            } else {
                storeAlarm(instanceName, message);
            }
        }

        if (executorService != null) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    doSendAlarm(instanceName, message);
                }
            });
        } else {
            doSendAlarm(instanceName, message);
        }
    }

    /**
     * 将异常信息持久化到数据库
     * @param instanceName
     * @param message
     */
    protected void storeAlarm(String instanceName, String message) {
        if (dataLogService != null) {
            dataLogService.insert(instanceName, message);
        }
    };

    protected abstract void doSendAlarm(String instanceName, String message);

    public void setExecutorService(GlobalExecutor executorService) {
        this.executorService = executorService;
    }

    public void setDataLogService(DataLogService dataLogService) {
        this.dataLogService = dataLogService;
    }
}
