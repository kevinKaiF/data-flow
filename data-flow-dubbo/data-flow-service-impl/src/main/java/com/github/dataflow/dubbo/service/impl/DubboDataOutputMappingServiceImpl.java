package com.github.dataflow.dubbo.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.dataflow.dubbo.dao.DataOutputMappingDao;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataOutputMappingService;
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
public class DubboDataOutputMappingServiceImpl implements DubboDataOutputMappingService {
    private final Logger logger = LoggerFactory.getLogger(DubboDataOutputMappingServiceImpl.class);
    private DataOutputMappingDao dataOutputMappingDao;

    public void setDataOutputMappingDao(DataOutputMappingDao dataOutputMappingDao) {
        this.dataOutputMappingDao = dataOutputMappingDao;
    }

    @Override
    public ServiceResult<Long> insert(DataOutputMapping dataOutputMapping) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            dataOutputMappingDao.insert(dataOutputMapping);
            result.setResult(dataOutputMapping.getId());
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataOutputMappingServiceImpl#insert]");
            logger.error("方法使用参数：[[dataOutputMapping:{}]]", dataOutputMapping.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用insert方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Integer> update(DataOutputMapping dataOutputMapping) {
        ServiceResult<Integer> result = new ServiceResult<Integer>();
        try {
            dataOutputMappingDao.update(dataOutputMapping);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataOutputMappingServiceImpl#update]");
            logger.error("方法使用参数：[[dataOutputMapping:{}]]", dataOutputMapping.toString());
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
            dataOutputMappingDao.delete(id);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataOutputMappingServiceImpl#delete]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用delete方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<DataOutputMapping> getById(Long id) {
        ServiceResult<DataOutputMapping> result = new ServiceResult<DataOutputMapping>();
        try {
            result.setResult(dataOutputMappingDao.getById(id));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataOutputMappingServiceImpl#getById]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用getById方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Long> countByCondition(DataOutputMapping dataOutputMapping) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            result.setResult(dataOutputMappingDao.countByCondition(dataOutputMapping));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataOutputMappingServiceImpl#countByCondition]");
            logger.error("方法使用参数：[[dataOutputMapping:{}]]", dataOutputMapping.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用countByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<List<DataOutputMapping>> findByCondition(DataOutputMapping dataOutputMapping, PageSet pageSet) {
        ServiceResult<List<DataOutputMapping>> result = new ServiceResult<List<DataOutputMapping>>();
        try {
            if (pageSet != null && pageSet.getPageNumber() != null && pageSet.getPageSize() != null) {
                PageHelper.startPage(pageSet.getPageNumber(), pageSet.getPageSize());
            }
            if (pageSet != null && !StringUtils.isBlank(pageSet.getSortColumns())) {
                PageHelper.orderBy(pageSet.getSortColumns());
            }
            result.setResult(dataOutputMappingDao.findByCondition(dataOutputMapping));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataOutputMappingServiceImpl#findByCondition]");
            logger.error("方法使用参数：[[dataOutputMapping:{}, pageSet:{}]]", dataOutputMapping.toString(), pageSet.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用findByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }
}
