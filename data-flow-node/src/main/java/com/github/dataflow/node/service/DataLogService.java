package com.github.dataflow.node.service;

import com.github.dataflow.dubbo.model.DataLog;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/26
 */
@Service
public class DataLogService {
    private Logger logger = LoggerFactory.getLogger(DataLogService.class);

    @Autowired
    private DubboDataLogService dubboDataLogService;

    public void insert(DataLog dataLog) {
        ServiceResult<Long> serviceResult = dubboDataLogService.insert(dataLog);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.node.service.DataLogService.insert",
                         "serviceResult", serviceResult.getErrorMessage());
        }
    }

    public void insert(String instanceName, String message) {
        DataLog dataLog = new DataLog();
        dataLog.setInstanceName(instanceName);
        dataLog.setMessage(message);
        dataLog.setCreateTime(new Date());
        insert(dataLog);
    }
}
