package com.github.dataflow.dubbo.service.impl;

import com.github.dataflow.dubbo.model.DataTable;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.service.DubboDataTableService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/2
 */
public class TestDataTableService extends TestBase {
    @Autowired
    private DubboDataTableService dataTableService;

    @Test
    public void testInsert() {
        DataTable dataTable = new DataTable();
        dataTable.setColumns("TEST Columns");
        dataTable.setDataInstanceId(2L);
        dataTable.setSchemaName("TEST SchemaName");
        dataTable.setTableName("TEST TableName");
        dataTableService.insert(dataTable);
    }

    @Test
    public void testUpdate() {
        DataTable dataTable = new DataTable();
        dataTable.setId(1L);
        dataTable.setColumns("TEST Columns");
        dataTable.setDataInstanceId(2L);
        dataTable.setSchemaName("TEST SchemaName");
        dataTable.setTableName("TEST TableName");
        dataTableService.update(dataTable);
    }

    @Test
    public void testGetById() {
        Long id = 2L;
        print(dataTableService.getById(id));
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        dataTableService.delete(id);
    }

    @Test
    public void testDeleteByDataInstanceId() {
        Long dataInstanceId = 2l;
        dataTableService.deleteByDataInstanceId(dataInstanceId);
    }

    @Test
    public void testFindByCondition() {
        DataTable dataTable = new DataTable();
        print(dataTableService.findByCondition(dataTable, new PageSet()));
    }
}


