package com.github.dataflow.dashboard.service;

import com.github.dataflow.dashboard.exception.DataFlowException;
import com.github.dataflow.dashboard.utils.Constants;
import com.github.dataflow.dubbo.model.DataLog;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/27
 */
@Service
public class DataLogService {
    private Logger logger = LoggerFactory.getLogger(DataLogService.class);

    @Autowired
    private DubboDataLogService dubboDataLogService;

    public Map getDataLogListByInstanceName(String instanceName, Integer start, Integer length) {
        DataLog dataLog = new DataLog();
        dataLog.setInstanceName(instanceName);
        ServiceResult<Long> longServiceResult = dubboDataLogService.countByCondition(dataLog);
        if (!longServiceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataLogService.getDataLogListByInstanceName",
                         "longServiceResult", longServiceResult.getErrorMessage());
            throw new DataFlowException();
        }
        Long total = longServiceResult.getResult();
        Map map = new HashMap();
        if (total == null || total == 0) {
            logger.warn("com.github.dataflow.dashboard.service.DataLogService.getDataLogListByInstanceName时未获取到结果");
            map.put(Constants.RECORDS_TOTAL, length);
            map.put(Constants.RECORDS_FILTERED, 0);
            map.put(Constants.DATA, Collections.EMPTY_LIST);
            return map;
        } else {
            PageSet pageSet = new PageSet(start, length);
            pageSet.setSortColumns(" CREATE_TIME DESC ");
            ServiceResult<List<DataLog>> listServiceResult = dubboDataLogService.findByCondition(dataLog, pageSet);
            if (!listServiceResult.isSuccess()) {
                logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataLogService.getDataLogListByInstanceName",
                             "listServiceResult", listServiceResult.getErrorMessage());
                throw new DataFlowException();
            }
            List<DataLog> dataLogList = listServiceResult.getResult();
            if (CollectionUtils.isEmpty(dataLogList)) {
                logger.warn("com.github.dataflow.dashboard.service.DataLogService.getDataLogListByInstanceName时未获取到结果");
                dataLogList = Collections.EMPTY_LIST;
            }
            map.put(Constants.RECORDS_TOTAL, length);
            map.put(Constants.RECORDS_FILTERED, total);
            map.put(Constants.DATA, dataLogList);
            return map;
        }
    }

    public DataLog getLogById(Long logId) {
        ServiceResult<DataLog> serviceResult = dubboDataLogService.getById(logId);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataLogService.getLogById",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }

        DataLog dataLog = serviceResult.getResult();
        if (dataLog == null) {
            logger.warn("com.github.dataflow.dashboard.service.DataLogService.getLogById时未获取到结果");
        }
        return dataLog;
    }
}
