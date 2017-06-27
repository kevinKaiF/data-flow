package com.github.dataflow.dubbo.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.dataflow.dubbo.dao.DataNodeConfigurationDao;
import com.github.dataflow.dubbo.model.DataNodeConfiguration;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataNodeConfigurationService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/5
 */
public class DubboDataNodeConfigurationServiceImpl implements DubboDataNodeConfigurationService {
    private final Logger logger = LoggerFactory.getLogger(DubboDataNodeConfigurationServiceImpl.class);
    private DataNodeConfigurationDao dataNodeConfigurationDao;

    public void setDataNodeConfigurationDao(DataNodeConfigurationDao dataNodeConfigurationDao) {
        this.dataNodeConfigurationDao = dataNodeConfigurationDao;
    }

    @Override
    public ServiceResult<Long> insert(DataNodeConfiguration dataNodeConfiguration) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            dataNodeConfigurationDao.insert(dataNodeConfiguration);
            result.setResult(dataNodeConfiguration.getId());
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataNodeConfigurationServiceImpl#insert]");
            logger.error("方法使用参数：[[dataNodeConfiguration:{}]]", dataNodeConfiguration.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用insert方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Integer> update(DataNodeConfiguration dataNodeConfiguration) {
        ServiceResult<Integer> result = new ServiceResult<Integer>();
        try {
            dataNodeConfigurationDao.update(dataNodeConfiguration);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataNodeConfigurationServiceImpl#update]");
            logger.error("方法使用参数：[[dataNodeConfiguration:{}]]", dataNodeConfiguration.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用update方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Integer> delete(Long id) {
        ServiceResult<Integer> result = new ServiceResult<Integer>();
        try {
            dataNodeConfigurationDao.delete(id);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataNodeConfigurationServiceImpl#delete]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用delete方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<DataNodeConfiguration> getById(Long id) {
        ServiceResult<DataNodeConfiguration> result = new ServiceResult<DataNodeConfiguration>();
        try {
            result.setResult(dataNodeConfigurationDao.getById(id));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataNodeConfigurationServiceImpl#getById]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用getById方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Long> countByCondition(DataNodeConfiguration dataNodeConfiguration) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            result.setResult(dataNodeConfigurationDao.countByCondition(dataNodeConfiguration));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataNodeConfigurationServiceImpl#countByCondition]");
            logger.error("方法使用参数：[[dataNodeConfiguration:{}]]", dataNodeConfiguration.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用countByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<List<DataNodeConfiguration>> findByCondition(DataNodeConfiguration dataNodeConfiguration, PageSet pageSet) {
        ServiceResult<List<DataNodeConfiguration>> result = new ServiceResult<List<DataNodeConfiguration>>();
        try {
            if (pageSet != null && pageSet.getPageNumber() != null && pageSet.getPageSize() != null) {
                PageHelper.startPage(pageSet.getPageNumber(), pageSet.getPageSize());
            }
            if (pageSet != null && !StringUtils.isBlank(pageSet.getSortColumns())) {
                PageHelper.orderBy(pageSet.getSortColumns());
            }
            result.setResult(dataNodeConfigurationDao.findByCondition(dataNodeConfiguration));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataNodeConfigurationServiceImpl#findByCondition]");
            logger.error("方法使用参数：[[dataNodeConfiguration:{}, pageSet:{}]]", dataNodeConfiguration.toString(), pageSet.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用findByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }
}
