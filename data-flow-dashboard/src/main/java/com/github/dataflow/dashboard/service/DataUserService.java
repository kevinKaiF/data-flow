package com.github.dataflow.dashboard.service;

import com.github.dataflow.dashboard.exception.DataFlowException;
import com.github.dataflow.dubbo.model.DataUser;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;
import com.github.dataflow.dubbo.service.DubboDataUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/7
 */
@Service
public class DataUserService {
    private static final Logger logger = LoggerFactory.getLogger(DataUserService.class);

    @Autowired
    private DubboDataUserService dubboDataUserService;

    public DataUser getUserByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        DataUser condition = new DataUser();
        condition.setUsername(username);
        ServiceResult<List<DataUser>> serviceResult = dubboDataUserService.findByCondition(condition, new PageSet());
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataUserService.getUserByUsername",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
        List<DataUser> dataUsers = serviceResult.getResult();
        if (CollectionUtils.isEmpty(dataUsers)) {
            logger.warn("com.github.dataflow.dashboard.service.DataUserService.getUserByUsername时未获取到结果");
            dataUsers = new ArrayList<>();
        }

        return dataUsers.get(0);
    }
}
