package com.github.dataflow.dubbo.service.impl;

import com.github.dataflow.dubbo.model.DataLog;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.service.DubboDataLogService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/26
 */
public class TestDataLogService extends TestBase {
    @Autowired
    private DubboDataLogService dubboDataLogService;

    @Test
    public void testInsert() {
        DataLog dataLog = new DataLog();
        dataLog.setInstanceName("test");
        dataLog.setMessage("xx error message");
        dataLog.setCreateTime(new Date());
        print(dubboDataLogService.insert(dataLog));
    }

    @Test
    public void testUpdate() {
        DataLog dataLog = new DataLog();
        dataLog.setId(1L);
        dataLog.setInstanceName("test");
        dataLog.setMessage("xx error message");
        dataLog.setCreateTime(new Date());
        dubboDataLogService.update(dataLog);
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        dubboDataLogService.delete(id);
    }

    @Test
    public void testCountByCondition() {
        DataLog dataLog = new DataLog();
        dataLog.setInstanceName("test");
        dataLog.setMessage("xx error message");
        print(dubboDataLogService.countByCondition(dataLog));
    }

    @Test
    public void testFindByCondition() {
        DataLog dataLog = new DataLog();
        dataLog.setInstanceName("test");
        dataLog.setMessage("xx error message");
        print(dubboDataLogService.findByCondition(dataLog, new PageSet()));
    }

    @Test
    public void testDeleteByCondition() {
        DataLog dataLog = new DataLog();
        dataLog.setInstanceName("xxxx");
        dataLog.setCreateTimeBegin(new Date());
        dataLog.setCreateTimeEnd(new Date());
        print(dubboDataLogService.deleteByCondition(dataLog));
    }
}
