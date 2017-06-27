package com.github.dataflow.dubbo.service.impl;


import com.alibaba.dubbo.common.json.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-deploy.xml"})
public class TestBase {
    protected void print(Object o) {
        try {
            System.out.println(JSON.json(o));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {

    }
}