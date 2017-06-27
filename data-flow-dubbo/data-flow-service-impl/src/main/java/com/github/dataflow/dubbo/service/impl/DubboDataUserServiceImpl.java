package com.github.dataflow.dubbo.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.dataflow.dubbo.dao.DataUserDao;
import com.github.dataflow.dubbo.model.DataUser;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataUserService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/7
 */
public class DubboDataUserServiceImpl implements DubboDataUserService {
    private final Logger logger = LoggerFactory.getLogger(DubboDataUserServiceImpl.class);
    private DataUserDao dataUserDao;

    public void setDataUserDao(DataUserDao dataUserDao) {
        this.dataUserDao = dataUserDao;
    }

    @Override
    public ServiceResult<Long> insert(DataUser dataUser) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            dataUserDao.insert(dataUser);
            result.setResult(dataUser.getId());
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataUserServiceImpl#insert]");
            logger.error("方法使用参数：[[dataUser:{}]]", dataUser.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用insert方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Integer> update(DataUser dataUser) {
        ServiceResult<Integer> result = new ServiceResult<Integer>();
        try {
            dataUserDao.update(dataUser);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataUserServiceImpl#update]");
            logger.error("方法使用参数：[[dataUser:{}]]", dataUser.toString());
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
            dataUserDao.delete(id);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataUserServiceImpl#delete]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用delete方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<DataUser> getById(Long id) {
        ServiceResult<DataUser> result = new ServiceResult<DataUser>();
        try {
            result.setResult(dataUserDao.getById(id));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataUserServiceImpl#getById]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用getById方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Long> countByCondition(DataUser dataUser) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            result.setResult(dataUserDao.countByCondition(dataUser));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataUserServiceImpl#countByCondition]");
            logger.error("方法使用参数：[[dataUser:{}]]", dataUser.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用countByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<List<DataUser>> findByCondition(DataUser dataUser, PageSet pageSet) {
        ServiceResult<List<DataUser>> result = new ServiceResult<List<DataUser>>();
        try {
            if (pageSet != null && pageSet.getPageNumber() != null && pageSet.getPageSize() != null) {
                PageHelper.startPage(pageSet.getPageNumber(), pageSet.getPageSize());
            }
            if (pageSet != null && !StringUtils.isBlank(pageSet.getSortColumns())) {
                PageHelper.orderBy(pageSet.getSortColumns());
            }
            result.setResult(dataUserDao.findByCondition(dataUser));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataUserServiceImpl#findByCondition]");
            logger.error("方法使用参数：[[dataUser:{}, pageSet:{}]]", dataUser.toString(), pageSet.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用findByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }
}
