package com.github.dataflow.dubbo.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.dataflow.dubbo.dao.DataSourceOutputDao;
import com.github.dataflow.dubbo.model.DataSourceOutput;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataSourceOutputService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/2
 */
public class DubboDataSourceOutputServiceImpl implements DubboDataSourceOutputService {
    private final Logger logger = LoggerFactory.getLogger(DubboDataSourceOutputServiceImpl.class);
    private DataSourceOutputDao dataSourceOutputDao;

    public void setDataSourceOutputDao(DataSourceOutputDao dataSourceOutputDao) {
        this.dataSourceOutputDao = dataSourceOutputDao;
    }

    @Override
    public ServiceResult<Long> insert(DataSourceOutput dataSourceOutput) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            dataSourceOutputDao.insert(dataSourceOutput);
            result.setResult(dataSourceOutput.getId());
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataSourceOutputServiceImpl#insert]");
            logger.error("方法使用参数：[[dataSourceOutput:{}]]", dataSourceOutput.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用insert方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Integer> update(DataSourceOutput dataSourceOutput) {
        ServiceResult<Integer> result = new ServiceResult<Integer>();
        try {
            dataSourceOutputDao.update(dataSourceOutput);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataSourceOutputServiceImpl#update]");
            logger.error("方法使用参数：[[dataSourceOutput:{}]]", dataSourceOutput.toString());
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
            dataSourceOutputDao.delete(id);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataSourceOutputServiceImpl#delete]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用delete方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<DataSourceOutput> getById(Long id) {
        ServiceResult<DataSourceOutput> result = new ServiceResult<DataSourceOutput>();
        try {
            result.setResult(dataSourceOutputDao.getById(id));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataSourceOutputServiceImpl#getById]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用getById方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Long> countByCondition(DataSourceOutput dataSourceOutput) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            result.setResult(dataSourceOutputDao.countByCondition(dataSourceOutput));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataSourceOutputServiceImpl#countByCondition]");
            logger.error("方法使用参数：[[dataSourceOutput:{}]]", dataSourceOutput.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用countByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<List<DataSourceOutput>> findByCondition(DataSourceOutput dataSourceOutput, PageSet pageSet) {
        ServiceResult<List<DataSourceOutput>> result = new ServiceResult<List<DataSourceOutput>>();
        try {
            if (pageSet != null && pageSet.getPageNumber() != null && pageSet.getPageSize() != null) {
                PageHelper.startPage(pageSet.getPageNumber(), pageSet.getPageSize());
            }
            if (pageSet != null && !StringUtils.isBlank(pageSet.getSortColumns())) {
                PageHelper.orderBy(pageSet.getSortColumns());
            }
            result.setResult(dataSourceOutputDao.findByCondition(dataSourceOutput));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataSourceOutputServiceImpl#findByCondition]");
            logger.error("方法使用参数：[[dataSourceOutput:{}, pageSet:{}]]", dataSourceOutput.toString(), pageSet.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用findByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }
}
