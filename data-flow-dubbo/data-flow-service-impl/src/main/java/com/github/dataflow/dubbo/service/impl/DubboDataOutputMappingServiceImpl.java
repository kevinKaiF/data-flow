package com.github.dataflow.dubbo.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.dataflow.dubbo.common.enums.DataInstanceStatus;
import com.github.dataflow.dubbo.dao.DataInstanceDao;
import com.github.dataflow.dubbo.dao.DataOutputMappingDao;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataOutputMappingService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

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
    private final String GLOBAL_SCHEMA_NAME = "*";
    ;

    public void setDataOutputMappingDao(DataOutputMappingDao dataOutputMappingDao) {
        this.dataOutputMappingDao = dataOutputMappingDao;
    }

    private DataInstanceDao dataInstanceDao;

    public void setDataInstanceDao(DataInstanceDao dataInstanceDao) {
        this.dataInstanceDao = dataInstanceDao;
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

    @Override
    public ServiceResult<Long> insertMapping(DataOutputMapping dataOutputMapping) {
        ServiceResult<Long> result = new ServiceResult<Long>();
        try {
            // 判断schemaName为*的情况
            validateGlobalSchemaName(dataOutputMapping);
            dataOutputMappingDao.insert(dataOutputMapping);
            // 更新dataInstance的状态为已创建状态，可以start
            dataInstanceDao.updateStatusById(dataOutputMapping.getDataInstanceId(), DataInstanceStatus.CREATED.getStatus());
            result.setResult(dataOutputMapping.getId());
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataOutputMappingServiceImpl#insertMapping]");
            logger.error("方法使用参数：[[dataOutputMapping:{}]]", dataOutputMapping.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用insertMapping方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    /**
     * 校验schemaName为*的数据是否已存在,即
     *
     * @param dataOutputMapping
     */
    private void validateGlobalSchemaName(DataOutputMapping dataOutputMapping) {
        if (GLOBAL_SCHEMA_NAME.equals(dataOutputMapping.getSchemaName())) {
            Long dataInstanceId = dataOutputMapping.getDataInstanceId();
            DataOutputMapping condition = new DataOutputMapping();
            condition.setDataInstanceId(dataInstanceId);
            condition.setSchemaName(GLOBAL_SCHEMA_NAME);
            List<DataOutputMapping> dataOutputMappings = dataOutputMappingDao.findByCondition(condition);
            if (!CollectionUtils.isEmpty(dataOutputMappings)) {
                throw new RuntimeException("DataOutputMapping schemaName[" + GLOBAL_SCHEMA_NAME + "] has existed.");
            }
        }
    }

    @Override
    public ServiceResult<Integer> updateMapping(DataOutputMapping dataOutputMapping) {
        ServiceResult<Integer> result = new ServiceResult<Integer>();
        try {
            dataOutputMappingDao.update(dataOutputMapping);
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataOutputMappingServiceImpl#updateMapping]");
            logger.error("方法使用参数：[[dataOutputMapping:{}]]", dataOutputMapping.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用updateMapping方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Integer> deleteMapping(Long id) {
        ServiceResult<Integer> result = new ServiceResult<Integer>();
        try {
            DataOutputMapping dataOutputMapping = dataOutputMappingDao.getById(id);
            dataOutputMappingDao.delete(id);
            // 更新dataInstance的状态
            DataOutputMapping condition = new DataOutputMapping();
            condition.setDataInstanceId(dataOutputMapping.getDataInstanceId());
            List<DataOutputMapping> dataOutputMappingList = dataOutputMappingDao.findByCondition(condition);
            if (CollectionUtils.isEmpty(dataOutputMappingList)) {
                dataInstanceDao.updateStatusById(dataOutputMapping.getDataInstanceId(), DataInstanceStatus.CREATING.getStatus());
            }
        } catch (Exception e) {
            logger.error("调用{}方法 异常", "[dataflow-service_DubboDataOutputMappingServiceImpl#deleteMapping]");
            logger.error("方法使用参数：[[id:{}]]", id.toString());
            logger.error("异常信息：{}", e);
            result.setSuccess(false);
            result.setErrorMessage("调用deleteMapping方法异常，异常信息：" + e.getMessage());
        }
        return result;
    }
}
