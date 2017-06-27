package com.github.dataflow.dashboard.service;

import com.github.dataflow.dashboard.exception.DataFlowException;
import com.github.dataflow.dashboard.utils.Constants;
import com.github.dataflow.dubbo.model.DataSourceOutput;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataSourceOutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/4
 */
@Service
public class DataSourceOutputService {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceOutputService.class);

    @Autowired
    private DubboDataSourceOutputService dubboDataSourceOutputService;

    public Long insert(DataSourceOutput dataSourceOutput) {
        if (dataSourceOutput.getId() != null) {
            return update(dataSourceOutput);
        }

        dataSourceOutput.setCreateTime(new Date());
        ServiceResult<Long> serviceResult = dubboDataSourceOutputService.insert(dataSourceOutput);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataSourceOutputService.insert",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }

        return serviceResult.getResult();
    }

    public Long update(DataSourceOutput dataSourceOutput) {
        dataSourceOutput.setUpdateTime(new Date());
        ServiceResult<Integer> serviceResult = dubboDataSourceOutputService.update(dataSourceOutput);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataSourceOutputService.update",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }

        return dataSourceOutput.getId();
    }

    public void delete(Long id) {
        ServiceResult<Integer> serviceResult = dubboDataSourceOutputService.delete(id);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataSourceOutputService.delete",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
    }

    public Map findByCondition(DataSourceOutput dataSourceOutput, Integer start, Integer length) {
        if (dataSourceOutput == null) {
            dataSourceOutput = new DataSourceOutput();
        }

        ServiceResult<Long> longServiceResult = dubboDataSourceOutputService.countByCondition(dataSourceOutput);
        if (!longServiceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataSourceOutputService.findByCondition",
                         "longServiceResult", longServiceResult.getErrorMessage());
            throw new DataFlowException();
        }

        Long total = longServiceResult.getResult();
        Map map = new HashMap();
        if (total == null || total == 0) {
            logger.warn("com.github.dataflow.dashboard.service.DataSourceOutputService.findByCondition时未获取到结果");
            map.put(Constants.RECORDS_TOTAL, length);
            map.put(Constants.RECORDS_FILTERED, 0);
            map.put(Constants.DATA, Collections.EMPTY_LIST);
            return map;
        } else {
            PageSet pageSet = new PageSet(start, length);
            ServiceResult<List<DataSourceOutput>> serviceResult = dubboDataSourceOutputService.findByCondition(dataSourceOutput, pageSet);
            if (!serviceResult.isSuccess()) {
                logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataSourceOutputService.findByCondition",
                             "serviceResult", serviceResult.getErrorMessage());
                throw new DataFlowException();
            }
            List<DataSourceOutput> dataSourceOutputList = serviceResult.getResult();
            if (CollectionUtils.isEmpty(dataSourceOutputList)) {
                logger.warn("com.github.dataflow.dashboard.service.DataSourceOutputService.findByCondition时未获取到结果");
                dataSourceOutputList = Collections.EMPTY_LIST;
            }

            map.put(Constants.RECORDS_TOTAL, length);
            map.put(Constants.RECORDS_FILTERED, total);
            map.put(Constants.DATA, dataSourceOutputList);
            return map;

        }
    }
}
