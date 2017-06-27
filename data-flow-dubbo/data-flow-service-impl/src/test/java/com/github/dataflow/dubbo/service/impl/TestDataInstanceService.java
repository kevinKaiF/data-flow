package com.github.dataflow.dubbo.service.impl;

import com.github.dataflow.dubbo.common.enums.DataInstanceStatus;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.service.DubboDataInstanceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/2
 */
public class TestDataInstanceService extends TestBase {
    @Autowired
    private DubboDataInstanceService dataInstanceService;

    @Test
    public void testInsert() {
        DataInstance dataInstance = new DataInstance();
        dataInstance.setBlackFilter("TEST.BlackFilter");
        dataInstance.setCreateTime(new java.util.Date());
        dataInstance.setHost("192.168.1.2");
        dataInstance.setJdbcUrl("jdbc:mysql://10.4.0.20:3306/dataflow");
        dataInstance.setName("name");
        dataInstance.setNodePath("/dataflow/node/192.168.1.1:8099");
        dataInstance.setOptions("{'name':'test'}");
        dataInstance.setPassword("123456");
        dataInstance.setPort(3306);
        dataInstance.setSlaveId(5514L);
        dataInstance.setStatus(0);
        dataInstance.setTransformScript("public List<RowDataMeta> transform(List<RowDataMeta> rowDataMetaList) {return rowDataMetaList}");
        dataInstance.setType(1);
        dataInstance.setUpdateTime(new java.util.Date());
        dataInstance.setUsername("TEST Username");
        dataInstance.setWhiteFilter("TEST.WhiteFilter");
        dataInstanceService.insert(dataInstance);
    }

    @Test
    public void testGetById() {
        Long id = 2L;
        print(dataInstanceService.getById(id));
    }

    @Test
    public void testGetByIdFully() {
        Long id = 12L;
        print(dataInstanceService.getByIdFully(id));
    }

    @Test
    public void testUpdate() {
        DataInstance dataInstance = new DataInstance();
        dataInstance.setId(1L);
        dataInstance.setBlackFilter("TEST.BlackFilter");
        dataInstance.setCreateTime(new java.util.Date());
        dataInstance.setHost("192.168.1.2");
        dataInstance.setJdbcUrl("jdbc:mysql://10.4.0.20:3306/dataflow");
        dataInstance.setName("name");
        dataInstance.setNodePath("/dataflow/node/192.168.1.1:8099");
        dataInstance.setOptions("{'name':'test'}");
        dataInstance.setPassword("123456");
        dataInstance.setPort(3306);
        dataInstance.setSlaveId(5514L);
        dataInstance.setStatus(DataInstanceStatus.CREATED.getStatus());
        dataInstance.setTransformScript("public List<RowDataMeta> transform(List<RowDataMeta> rowDataMetaList) {return rowDataMetaList}");
        dataInstance.setType(1);
        dataInstance.setUpdateTime(new java.util.Date());
        dataInstance.setUsername("TEST Username");
        dataInstance.setWhiteFilter("TEST.WhiteFilter");
        dataInstanceService.update(dataInstance);
    }

    @Test
    public void testFindByCondition() {
        DataInstance dataInstance = new DataInstance();
        dataInstance.setBlackFilter("TEST.BlackFilter");
        dataInstance.setHost("192.168.1.2");
        dataInstance.setJdbcUrl("jdbc:mysql://10.4.0.20:3306/dataflow");
        dataInstance.setName("name");
        dataInstance.setNodePath("/dataflow/node/192.168.1.1:8099");
        dataInstance.setOptions("{'name':'test'}");
        dataInstance.setPassword("123456");
        dataInstance.setPort(3306);
        dataInstance.setSlaveId(5514L);
        dataInstance.setStatus(DataInstanceStatus.CREATED.getStatus());
        dataInstance.setTransformScript("public List<RowDataMeta> transform(List<RowDataMeta> rowDataMetaList) {return rowDataMetaList}");
        dataInstance.setType(1);
        dataInstance.setUsername("TEST Username");
        dataInstance.setWhiteFilter("TEST.WhiteFilter");

        PageSet pageSet = new PageSet();
        print(dataInstanceService.findByCondition(dataInstance, pageSet));
    }

    @Test
    public void testFindByConditionFully() {
        DataInstance dataInstance = new DataInstance();
        dataInstance.setBlackFilter("TEST.BlackFilter");
        dataInstance.setHost("192.168.1.2");
        dataInstance.setJdbcUrl("jdbc:mysql://10.4.0.20:3306/dataflow");
        dataInstance.setName("name");
//        dataInstance.setNodePath("/dataflow/node/192.168.1.1:8099");
//        dataInstance.setOptions("{'name':'test'}");
//        dataInstance.setPassword("123456");
//        dataInstance.setPort(3306);
//        dataInstance.setSlaveId(5514L);
//        dataInstance.setStatus(DataInstanceStatus.CREATED.getStatus());
//        dataInstance.setTransformScript("public List<RowDataMeta> transform(List<RowDataMeta> rowDataMetaList) {return rowDataMetaList}");
//        dataInstance.setType(1);
//        dataInstance.setUsername("TEST Username");
//        dataInstance.setWhiteFilter("TEST.WhiteFilter");

        PageSet pageSet = new PageSet();
        print(dataInstanceService.findByConditionFully(dataInstance, pageSet));
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        dataInstanceService.delete(id);
    }

    @Test
    public void testDeleteIfStopped() {
        Long id = 1L;
        dataInstanceService.deleteIfStopped(id);
    }

    @Test
    public void testUpdateStatusByName() {
        String name = "name";
        dataInstanceService.updateStatusByName(name, DataInstanceStatus.STOP.getStatus());
    }
}


