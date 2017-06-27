package com.github.dataflow.dubbo.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.dataflow.dubbo.dao.DataTableDao;
import com.github.dataflow.dubbo.model.DataTable;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataTableService;
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
public class DubboDataTableServiceImpl implements DubboDataTableService {
    private final Logger logger = LoggerFactory.getLogger(DubboDataTableServiceImpl.class);
    private DataTableDao dataTableDao;

    public void setDataTableDao(DataTableDao dataTableDao) {
        this.dataTableDao = dataTableDao;
    }

    @Override
    public ServiceResult<Long> insert(DataTable dataTable) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            dataTableDao.insert(dataTable);
            result.setResult(dataTable.getId());
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataTableServiceImpl#insert]");
            logger.error("方法使用参数：[[dataTable:{}]]", dataTable.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用insert方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Integer> update(DataTable dataTable) {
        ServiceResult<Integer> result = new ServiceResult<Integer>();
        try {
            dataTableDao.update(dataTable);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataTableServiceImpl#update]");
            logger.error("方法使用参数：[[dataTable:{}]]", dataTable.toString());
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
            dataTableDao.delete(id);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataTableServiceImpl#delete]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用delete方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<DataTable> getById(Long id) {
        ServiceResult<DataTable> result = new ServiceResult<DataTable>();
        try {
            result.setResult(dataTableDao.getById(id));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataTableServiceImpl#getById]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用getById方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Void> deleteByDataInstanceId(Long dataInstanceId) {
        ServiceResult<Void> result = new ServiceResult<Void>();
        try {
            dataTableDao.deleteByDataInstanceId(dataInstanceId);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataTableServiceImpl#deleteByDataInstanceId]");
            logger.error("方法使用参数：[[dataInstanceId:{}]]", dataInstanceId.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用deleteByDataInstanceId方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<List<DataTable>> findByCondition(DataTable dataTable, PageSet pageSet) {
        ServiceResult<List<DataTable>> result = new ServiceResult<List<DataTable>>();
        try {
            if (pageSet != null && pageSet.getPageNumber() != null && pageSet.getPageSize() != null) {
                PageHelper.startPage(pageSet.getPageNumber(), pageSet.getPageSize());
            }
            if (pageSet != null && !StringUtils.isBlank(pageSet.getSortColumns())) {
                PageHelper.orderBy(pageSet.getSortColumns());
            }
            result.setResult(dataTableDao.findByCondition(dataTable));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataTableServiceImpl#findByCondition]");
            logger.error("方法使用参数：[[dataTable:{}, pageSet:{}]]", dataTable.toString(), pageSet.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用findByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }
}
