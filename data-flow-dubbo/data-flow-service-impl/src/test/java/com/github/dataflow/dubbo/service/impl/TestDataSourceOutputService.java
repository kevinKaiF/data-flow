package com.github.dataflow.dubbo.service.impl;

import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataSourceOutput;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.service.DubboDataSourceOutputService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/2
 */
public class TestDataSourceOutputService extends TestBase {
    @Autowired
    private DubboDataSourceOutputService dataSourceOutputService;

    @Test
    public void testInsert() {
        DataSourceOutput dataSourceOutput = new DataSourceOutput();
        dataSourceOutput.setCreateTime(new java.util.Date());
        dataSourceOutput.setOptions("{'name':'kevin'}");
        dataSourceOutput.setPassword("TEST Password");
        dataSourceOutput.setType(DataSourceType.KAFKA.getType());
        dataSourceOutput.setUsername("TEST Username");
        dataSourceOutputService.insert(dataSourceOutput);
    }

    @Test
    public void testUpdate() {
        DataSourceOutput dataSourceOutput = new DataSourceOutput();
        dataSourceOutput.setId(1L);
        dataSourceOutput.setCreateTime(new java.util.Date());
        dataSourceOutput.setOptions("{'name':'kevin'}");
        dataSourceOutput.setPassword("TEST Password");
        dataSourceOutput.setType(DataSourceType.KAFKA.getType());
        dataSourceOutput.setUsername("TEST Username");
        dataSourceOutput.setUpdateTime(new Date());
        dataSourceOutputService.update(dataSourceOutput);
    }

    @Test
    public void testGetById() {
        Long id = 2L;
        print(dataSourceOutputService.getById(id));
    }

    @Test
    public void testFindByCondition() {
        DataSourceOutput dataSourceOutput = new DataSourceOutput();
        dataSourceOutput.setId(1L);
        dataSourceOutput.setCreateTime(new java.util.Date());
        dataSourceOutput.setOptions("{'name':'kevin'}");
        dataSourceOutput.setPassword("TEST Password");
        dataSourceOutput.setType(DataSourceType.KAFKA.getType());
        dataSourceOutput.setUsername("TEST Username");

        PageSet pageSet = new PageSet();
        print(dataSourceOutputService.findByCondition(dataSourceOutput, pageSet));
    }

    @Test
    public void testCountByCondition() {
        DataSourceOutput dataSourceOutput = new DataSourceOutput();
        dataSourceOutput.setId(1L);
        dataSourceOutput.setCreateTime(new java.util.Date());
        dataSourceOutput.setOptions("{'name':'kevin'}");
        dataSourceOutput.setPassword("TEST Password");
        dataSourceOutput.setType(DataSourceType.KAFKA.getType());
        dataSourceOutput.setUsername("TEST Username");
        print(dataSourceOutputService.countByCondition(dataSourceOutput));
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        dataSourceOutputService.delete(id);
    }

}


