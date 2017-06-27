package com.github.dataflow.dubbo.service.impl;

import com.github.dataflow.dubbo.common.enums.DataAlarmType;
import com.github.dataflow.dubbo.model.DataNodeConfiguration;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.service.DubboDataNodeConfigurationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/5
 */
public class TestDataNodeConfigService extends TestBase {
    @Autowired
    private DubboDataNodeConfigurationService dubboDataNodeConfigurationService;

    @Test
    public void testInsert() {
        DataNodeConfiguration dataNodeConfiguration = new DataNodeConfiguration();
        dataNodeConfiguration.setOptions("{'name':'kevin'}");
        dataNodeConfiguration.setType(DataAlarmType.MAIL.getType());
        print(dubboDataNodeConfigurationService.insert(dataNodeConfiguration));
    }

    @Test
    public void testUpdate() {
        DataNodeConfiguration dataNodeConfiguration = new DataNodeConfiguration();
        dataNodeConfiguration.setId(1L);
        dataNodeConfiguration.setOptions("{'name':'kevin'}");
        dataNodeConfiguration.setType(DataAlarmType.MAIL.getType());
        print(dubboDataNodeConfigurationService.update(dataNodeConfiguration));
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        dubboDataNodeConfigurationService.delete(id);
    }

    @Test
    public void testFindByCondition() {
        DataNodeConfiguration dataNodeConfiguration = new DataNodeConfiguration();
        dataNodeConfiguration.setId(1L);
        dataNodeConfiguration.setOptions("{'name':'kevin'}");
        dataNodeConfiguration.setType(DataAlarmType.MAIL.getType());
        print(dubboDataNodeConfigurationService.findByCondition(dataNodeConfiguration, new PageSet()));
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        print(dubboDataNodeConfigurationService.getById(id));
    }


}


