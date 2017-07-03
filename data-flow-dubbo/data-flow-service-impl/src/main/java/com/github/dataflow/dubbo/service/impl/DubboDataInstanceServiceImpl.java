package com.github.dataflow.dubbo.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.dataflow.dubbo.dao.DataInstanceDao;
import com.github.dataflow.dubbo.dao.DataOutputMappingDao;
import com.github.dataflow.dubbo.dao.DataTableDao;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataInstanceService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/1
 */
public class DubboDataInstanceServiceImpl implements DubboDataInstanceService {
    private final Logger logger = LoggerFactory.getLogger(DubboDataInstanceServiceImpl.class);
    private DataInstanceDao dataInstanceDao;

    private DataTableDao dataTableDao;

    private DataOutputMappingDao dataOutputMappingDao;

    public void setDataTableDao(DataTableDao dataTableDao) {
        this.dataTableDao = dataTableDao;
    }

    public void setDataOutputMappingDao(DataOutputMappingDao dataOutputMappingDao) {
        this.dataOutputMappingDao = dataOutputMappingDao;
    }

    public void setDataInstanceDao(DataInstanceDao dataInstanceDao) {
        this.dataInstanceDao = dataInstanceDao;
    }

    @Override
    public ServiceResult<Long> insert(DataInstance dataInstance) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            List<DataInstance> dataInstances = dataInstanceDao.getByName(dataInstance.getName());
            if (CollectionUtils.isEmpty(dataInstances)) {
                dataInstanceDao.insert(dataInstance);
                result.setResult(dataInstance.getId());
            } else {
                throw new RuntimeException("DataInstance [" + dataInstance + "] has existed.");
            }
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#insert]");
            logger.error("方法使用参数：[[dataInstance:{}]]", dataInstance.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用insert方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Integer> update(DataInstance dataInstance) {
        ServiceResult<Integer> result = new ServiceResult<Integer>();
        try {
            List<DataInstance> dataInstances = dataInstanceDao.getByName(dataInstance.getName());
            if (CollectionUtils.isEmpty(dataInstances)) {
                dataInstanceDao.update(dataInstance);
            } else {
                if (dataInstances.size() == 1) {
                    if (dataInstances.get(0).getId().equals(dataInstance.getId())) {
                        dataInstanceDao.update(dataInstance);
                    } else {
                        throw new RuntimeException("DataInstance [" + dataInstance + "] has existed.");
                    }
                } else {
                    throw new RuntimeException("DataInstance [" + dataInstance + "] has existed.");
                }
            }
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#update]");
            logger.error("方法使用参数：[[dataInstance:{}]]", dataInstance.toString());
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
            dataInstanceDao.delete(id);
            // 同时删除dataTable,dataOutputMapping
            dataTableDao.deleteByDataInstanceId(id);
            dataOutputMappingDao.deleteByDataInstanceId(id);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#delete]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用delete方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Integer> deleteBatch(List<Long> ids) {
        ServiceResult<Integer> result = new ServiceResult<Integer>();
        try {
            dataInstanceDao.deleteBatch(ids);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#deleteBatch]");
            logger.error("方法使用参数：[[ids:{}]]", ids.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用deleteBatch方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<DataInstance> getById(Long id) {
        ServiceResult<DataInstance> result = new ServiceResult<DataInstance>();
        try {
            result.setResult(dataInstanceDao.getById(id));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#getById]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用getById方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Long> countByCondition(DataInstance dataInstance) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            result.setResult(dataInstanceDao.countByCondition(dataInstance));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#countByCondition]");
            logger.error("方法使用参数：[[dataInstance:{}]]", dataInstance.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用countByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<List<DataInstance>> findByCondition(DataInstance dataInstance, PageSet pageSet) {
        ServiceResult<List<DataInstance>> result = new ServiceResult<List<DataInstance>>();
        try {
            if (pageSet != null && pageSet.getPageNumber() != null && pageSet.getPageSize() != null) {
                PageHelper.startPage(pageSet.getPageNumber(), pageSet.getPageSize());
            }
            if (pageSet != null && !StringUtils.isBlank(pageSet.getSortColumns())) {
                PageHelper.orderBy(pageSet.getSortColumns());
            }
            result.setResult(dataInstanceDao.findByCondition(dataInstance));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#findByCondition]");
            logger.error("方法使用参数：[[dataInstance:{}, pageSet:{}]]", dataInstance.toString(), pageSet.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用findByCondition方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Void> updateStatusByName(String name, Integer status) {
        ServiceResult<Void> result = new ServiceResult<Void>();
        try {
            dataInstanceDao.updateStatusByName(name, status);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#updateStatusByName]");
            logger.error("方法使用参数：[[name:{}, status:{}]]", name.toString(), status.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用updateStatusByName方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Void> updateStatusById(Long id, Integer status) {
        ServiceResult<Void> result = new ServiceResult<Void>();
        try {
            dataInstanceDao.updateStatusById(id, status);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#updateStatusById]");
            logger.error("方法使用参数：[[id:{}, status:{}]]", id.toString(), status.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用updateStatusById方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Void> deleteIfStopped(Long id) {
        ServiceResult<Void> result = new ServiceResult<Void>();
        try {
            dataInstanceDao.deleteIfStopped(id);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#deleteIfStopped]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用deleteIfStopped方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<DataInstance> getByIdFully(Long id) {
        ServiceResult<DataInstance> result = new ServiceResult<DataInstance>();
        try {
            result.setResult(dataInstanceDao.getByIdFully(id));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#getByIdFully]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用getByIdFully方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<List<DataInstance>> findByConditionFully(DataInstance dataInstance, PageSet pageSet) {
        ServiceResult<List<DataInstance>> result = new ServiceResult<List<DataInstance>>();
        try {
            if (pageSet != null && pageSet.getPageNumber() != null && pageSet.getPageSize() != null) {
                PageHelper.startPage(pageSet.getPageNumber(), pageSet.getPageSize());
            }
            if (pageSet != null && !StringUtils.isBlank(pageSet.getSortColumns())) {
                PageHelper.orderBy(pageSet.getSortColumns());
            }
            result.setResult(dataInstanceDao.findByConditionFully(dataInstance));
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataInstanceServiceImpl#findByConditionFully]");
            logger.error("方法使用参数：[[dataInstance:{}, pageSet:{}]]", dataInstance.toString(), pageSet.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用findByConditionFully方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }
}
