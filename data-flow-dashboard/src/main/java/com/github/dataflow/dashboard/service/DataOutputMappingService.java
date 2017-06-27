package com.github.dataflow.dashboard.service;

import com.github.dataflow.dashboard.exception.DataFlowException;
import com.github.dataflow.dashboard.utils.Constants;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataOutputMappingService;
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
 * @date : 2017/6/4
 */
@Service
public class DataOutputMappingService {
    private static final Logger logger = LoggerFactory.getLogger(DataOutputMappingService.class);

    @Autowired
    private DubboDataOutputMappingService dubboDataOutputMappingService;

    public Long insert(DataOutputMapping dataOutputMapping) {
        if (dataOutputMapping.getId() != null) {
            return update(dataOutputMapping);
        }

        ServiceResult<Long> serviceResult = dubboDataOutputMappingService.insert(dataOutputMapping);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataOutputMappingService.insert",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
        return serviceResult.getResult();
    }

    public Long update(DataOutputMapping dataOutputMapping) {
        ServiceResult<Integer> serviceResult = dubboDataOutputMappingService.update(dataOutputMapping);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataOutputMappingService.update",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }

        return dataOutputMapping.getId();
    }

    public void delete(Long id) {
        ServiceResult<Integer> serviceResult = dubboDataOutputMappingService.delete(id);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataOutputMappingService.delete",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
    }

    public Map findByCondition(DataOutputMapping dataOutputMapping, Integer start, Integer length) {
        if (dataOutputMapping == null) {
            dataOutputMapping = new DataOutputMapping();
        }

        ServiceResult<Long> serviceResult = dubboDataOutputMappingService.countByCondition(dataOutputMapping);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataOutputMappingService.findByCondition",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
        Long total = serviceResult.getResult();
        Map map = new HashMap<>();
        if (total == null || total == 0) {
            logger.warn("com.github.dataflow.dashboard.service.DataOutputMappingService.findByCondition时未获取到结果");
            map.put(Constants.RECORDS_TOTAL, total);
            map.put(Constants.RECORDS_FILTERED, 0);
            map.put(Constants.DATA, Collections.EMPTY_LIST);
            return map;
        } else {
            PageSet pageSet = new PageSet(start, length);
            ServiceResult<List<DataOutputMapping>> listServiceResult = dubboDataOutputMappingService.findByCondition(dataOutputMapping, pageSet);
            if (!listServiceResult.isSuccess()) {
                logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataOutputMappingService.findByCondition",
                             "listServiceResult", listServiceResult.getErrorMessage());
                throw new DataFlowException();
            }
            List<DataOutputMapping> dataOutputMappings = listServiceResult.getResult();
            if (CollectionUtils.isEmpty(dataOutputMappings)) {
                logger.warn("com.github.dataflow.dashboard.service.DataOutputMappingService.findByCondition时未获取到结果");
                dataOutputMappings = Collections.EMPTY_LIST;
            }

            map.put(Constants.RECORDS_TOTAL, length);
            map.put(Constants.RECORDS_FILTERED, total);
            map.put(Constants.DATA, dataOutputMappings);
            return map;
        }
    }
}
