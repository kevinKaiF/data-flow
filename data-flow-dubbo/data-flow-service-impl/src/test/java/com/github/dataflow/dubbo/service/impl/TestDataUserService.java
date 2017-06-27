package com.github.dataflow.dubbo.service.impl;

import com.github.dataflow.dubbo.model.DataUser;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.service.DubboDataUserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/7
 */
public class TestDataUserService extends TestBase {
    @Autowired
    private DubboDataUserService dubboDataUserService;

    @Test
    public void testInsert() {
        DataUser dataUser = new DataUser();
        dataUser.setId(3L);
        dataUser.setPassword("TEST Password");
        dataUser.setUsername("TEST Username");
        dubboDataUserService.insert(dataUser);
    }

    @Test
    public void testUpdate() {
        DataUser dataUser = new DataUser();
        dataUser.setId(3L);
        dataUser.setPassword("Password");
        dataUser.setUsername("Username");
        dubboDataUserService.update(dataUser);
    }

    @Test
    public void testGetById() {
        Long id = 3L;
        print(dubboDataUserService.getById(id));
    }

    @Test
    public void testDelete() {
        Long id = 3L;
        dubboDataUserService.delete(id);
    }

    @Test
    public void testFindByCondition() {
        DataUser dataUser = new DataUser();
        dataUser.setId(3L);
        dataUser.setPassword("Password");
        dataUser.setUsername("Username");
        print(dubboDataUserService.findByCondition(dataUser, new PageSet()));
    }

}


