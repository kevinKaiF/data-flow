package com.github.dataflow.dashboard.service;

import com.github.dataflow.dashboard.exception.DataFlowException;
import com.github.dataflow.dubbo.model.DataTable;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/4
 */
@Service
public class DataTableService {
    private static final Logger logger = LoggerFactory.getLogger(DataTableService.class);

    @Autowired
    private DubboDataTableService dubboDataTableService;

    public Long insert(DataTable dataTable) {
        if (dataTable.getId() != null) {
            return update(dataTable);
        }

        ServiceResult<Long> serviceResult = dubboDataTableService.insert(dataTable);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataTableService.insert",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }

        return serviceResult.getResult();
    }

    public Long update(DataTable dataTable) {
        ServiceResult<Integer> serviceResult = dubboDataTableService.update(dataTable);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataTableService.update",
                         "serviceResult.getResult()", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }

        return dataTable.getId();
    }

    public void delete(Long id) {
        ServiceResult<Integer> serviceResult = dubboDataTableService.delete(id);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataTableService.delete",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
    }
}
