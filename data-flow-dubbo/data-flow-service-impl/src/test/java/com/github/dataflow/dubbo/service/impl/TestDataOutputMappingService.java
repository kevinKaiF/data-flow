package com.github.dataflow.dubbo.service.impl;

import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.service.DubboDataOutputMappingService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/2
 */
public class TestDataOutputMappingService extends TestBase {
    @Autowired
    private DubboDataOutputMappingService dataOutputMappingService;

    @Test
    public void testInsert() {
        DataOutputMapping dataOutputMapping = new DataOutputMapping();
        dataOutputMapping.setDataInstanceId(2L);
        dataOutputMapping.setOptions("{'name' :'kevin'}");
        dataOutputMapping.setSchemaName("TEST");
        dataOutputMapping.setDataSourceOutputId(2L);
        dataOutputMappingService.insert(dataOutputMapping);
    }

    @Test
    public void testGetById() {
        Long id = 21L;
        print(dataOutputMappingService.getById(id));
    }

    @Test
    public void testUpdate() {
        DataOutputMapping dataOutputMapping = new DataOutputMapping();
        dataOutputMapping.setDataInstanceId(2L);
        dataOutputMapping.setId(1L);
        dataOutputMapping.setOptions("{'name' :'kevin'}");
        dataOutputMapping.setSchemaName("TEST");
        dataOutputMapping.setDataSourceOutputId(2L);
        dataOutputMappingService.update(dataOutputMapping);
    }

    @Test
    public void testDelete() {
        Long id = 10L;
        dataOutputMappingService.delete(id);
    }

    @Test
    public void testFindByCondition() {
        DataOutputMapping dataOutputMapping = new DataOutputMapping();
        dataOutputMapping.setDataInstanceId(2L);
        dataOutputMapping.setOptions("{'name' :'kevin'}");
        dataOutputMapping.setSchemaName("TEST");
        dataOutputMapping.setDataSourceOutputId(2L);
        print(dataOutputMappingService.findByCondition(dataOutputMapping, new PageSet()));
    }

}


