package com.github.dataflow.dashboard.service;

import com.github.dataflow.dashboard.exception.DataFlowException;
import com.github.dataflow.dubbo.model.DataNodeConfiguration;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataNodeConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/14
 */
@Service
public class DataNodeConfigurationService {
    private static final Logger logger = LoggerFactory.getLogger(DataNodeConfigurationService.class);

    @Autowired
    private DubboDataNodeConfigurationService dubboDataNodeConfigurationService;

    public Long insert(DataNodeConfiguration dataNodeConfiguration) {
        if (dataNodeConfiguration.getId() != null) {
            return update(dataNodeConfiguration);
        }

        ServiceResult<Long> serviceResult = dubboDataNodeConfigurationService.insert(dataNodeConfiguration);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataNodeConfigurationService.insert",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
        return serviceResult.getResult();
    }

    public Long update(DataNodeConfiguration dataNodeConfiguration) {
        ServiceResult<Integer> serviceResult = dubboDataNodeConfigurationService.update(dataNodeConfiguration);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataNodeConfigurationService.update",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
        return dataNodeConfiguration.getId();
    }

    public void delete(Long id) {
        ServiceResult<Integer> serviceResult = dubboDataNodeConfigurationService.delete(id);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataNodeConfigurationService.delete",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
    }

    public DataNodeConfiguration get() {
        ServiceResult<List<DataNodeConfiguration>> listServiceResult = dubboDataNodeConfigurationService.findByCondition(new DataNodeConfiguration(), new PageSet());
        if (!listServiceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataNodeConfigurationService.get",
                         "listServiceResult", listServiceResult.getErrorMessage());
            throw new DataFlowException();
        }
        List<DataNodeConfiguration> dataNodeConfigurations = listServiceResult.getResult();
        if (CollectionUtils.isEmpty(dataNodeConfigurations)) {
            logger.warn("com.github.dataflow.dashboard.service.DataNodeConfigurationService.get时未获取到结果");
            return new DataNodeConfiguration();
        } else {
            return dataNodeConfigurations.get(0);
        }
    }
}
