package com.github.dataflow.node.service;

import com.github.dataflow.dubbo.model.DataNodeConfiguration;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataNodeConfigurationService;
import com.github.dataflow.node.exception.InstanceException;
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
 * @date : 2017/6/26
 */
@Service
public class DataNodeConfigurationService {
    private Logger logger = LoggerFactory.getLogger(DataNodeConfiguration.class);

    @Autowired
    private DubboDataNodeConfigurationService dubboDataNodeConfigurationService;

    public DataNodeConfiguration getDataNodeConfiguration() {
        DataNodeConfiguration condition = new DataNodeConfiguration();
        ServiceResult<List<DataNodeConfiguration>> serviceResult = dubboDataNodeConfigurationService.findByCondition(condition, new PageSet());
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.node.service.DataNodeConfigurationService.getDataNodeConfiguration",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new InstanceException();
        }
        List<DataNodeConfiguration> dataNodeConfigurationList = serviceResult.getResult();
        if (CollectionUtils.isEmpty(dataNodeConfigurationList)) {
            logger.warn("com.github.dataflow.node.service.DataNodeConfigurationService.getDataNodeConfiguration时未获取到结果");
            return null;
        } else {
            return dataNodeConfigurationList.get(0);
        }
    }
}
