package com.github.dataflow.dubbo.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.dataflow.dubbo.dao.DataLogDao;
import com.github.dataflow.dubbo.model.DataLog;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataLogService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/26
 */
public class DubboDataLogServiceImpl implements DubboDataLogService {
    private final Logger logger = LoggerFactory.getLogger(DubboDataLogServiceImpl.class);
    private DataLogDao dataLogDao;

    public void setDataLogDao(DataLogDao dataLogDao) {
        this.dataLogDao = dataLogDao;
    }

    @Override
    public ServiceResult<Long> insert(DataLog dataLog) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            dataLogDao.insert(dataLog);
            result.setResult(dataLog.getId());
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataLogServiceImpl#insert]");
            logger.error("方法使用参数：[[dataLog:{}]]", dataLog.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用insert方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Integer> update(DataLog dataLog) {
        ServiceResult<Integer> result = new ServiceResult<Integer>();
        try {
            dataLogDao.update(dataLog);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataLogServiceImpl#update]");
            logger.error("方法使用参数：[[dataLog:{}]]", dataLog.toString());
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
            dataLogDao.delete(id);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataLogServiceImpl#delete]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用delete方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<DataLog> getById(Long id) {
        ServiceResult<DataLog> result = new ServiceResult<DataLog>();
        try {
            result.setResult(dataLogDao.getById(id));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataLogServiceImpl#getById]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用getById方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Long> countByCondition(DataLog dataLog) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            result.setResult(dataLogDao.countByCondition(dataLog));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataLogServiceImpl#countByCondition]");
            logger.error("方法使用参数：[[dataLog:{}]]", dataLog.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用countByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<List<DataLog>> findByCondition(DataLog dataLog, PageSet pageSet) {
        ServiceResult<List<DataLog>> result = new ServiceResult<List<DataLog>>();
        try {
            if (pageSet != null && pageSet.getPageNumber() != null && pageSet.getPageSize() != null) {
                PageHelper.startPage(pageSet.getPageNumber(), pageSet.getPageSize());
            }
            if (pageSet != null && !StringUtils.isBlank(pageSet.getSortColumns())) {
                PageHelper.orderBy(pageSet.getSortColumns());
            }
            result.setResult(dataLogDao.findByCondition(dataLog));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataLogServiceImpl#findByCondition]");
            logger.error("方法使用参数：[[dataLog:{}, pageSet:{}]]", dataLog.toString(), pageSet.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用findByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<List<DataLog>> findByConditionFully(DataLog dataLog, PageSet pageSet) {
        ServiceResult<List<DataLog>> result = new ServiceResult<>();
        try {
            if (pageSet != null && pageSet.getPageNumber() != null && pageSet.getPageSize() != null) {
                PageHelper.startPage(pageSet.getPageNumber(), pageSet.getPageSize());
            }
            if (pageSet != null && !StringUtils.isBlank(pageSet.getSortColumns())) {
                PageHelper.orderBy(pageSet.getSortColumns());
            }
            result.setResult(dataLogDao.findByConditionFully(dataLog));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataLogServiceImpl#findByConditionFully]");
            logger.error("方法使用参数：[[dataLog:{}, pageSet:{}]]", dataLog.toString(), pageSet.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用findByConditionFully方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Void> deleteByCondition(DataLog dataLog) {
        ServiceResult<Void> result = new ServiceResult<Void>();
        try {
            dataLogDao.deleteByCondition(dataLog);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataLogServiceImpl#deleteByCondition]");
            logger.error("方法使用参数：[[dataLog:{}]]", dataLog.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用deleteByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }
}
