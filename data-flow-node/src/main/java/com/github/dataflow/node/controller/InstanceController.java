package com.github.dataflow.node.controller;

import com.alibaba.fastjson.JSON;
import com.github.dataflow.common.model.ResponseEntity;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.node.service.InstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author kevin
 * @date 2017-05-28 2:45 PM.
 */
@Controller
@RequestMapping("instance")
public class InstanceController {
    private Logger logger = LoggerFactory.getLogger(InstanceController.class);

    @Autowired
    private InstanceService instanceService;

    @RequestMapping("start")
    @ResponseBody
    public ResponseEntity start(DataInstance dataInstance) {
        ResponseEntity responseEntity = new ResponseEntity(ResponseEntity.SUCCESS);
        try {
            instanceService.start(dataInstance);
        } catch (Exception e) {
            logger.error("start Instance [name : {}] failure. detail : ",
                         new Object[]{dataInstance.getName(), e});
            responseEntity.setResponseStatus(ResponseEntity.FAILURE);
            responseEntity.setMessage(e.getMessage());
        }
        return responseEntity;
    }

    @RequestMapping("startWithJson")
    @ResponseBody
    public ResponseEntity start(@RequestParam("dataInstance") String dataInstanceJson) {
        ResponseEntity responseEntity = new ResponseEntity(ResponseEntity.SUCCESS);
        DataInstance dataInstance = null;
        try {
            dataInstance = JSON.parseObject(dataInstanceJson, DataInstance.class);
            instanceService.start(dataInstance);
        } catch (Exception e) {
            logger.error("start Instance [name : {}] failure. detail : ",
                         new Object[]{dataInstance.getName(), e});
            responseEntity.setResponseStatus(ResponseEntity.FAILURE);
            responseEntity.setMessage(e.getMessage());
        }
        return responseEntity;
    }

    @RequestMapping("stop")
    @ResponseBody
    public ResponseEntity stop(DataInstance dataInstance) {
        ResponseEntity responseEntity = new ResponseEntity(ResponseEntity.SUCCESS);
        try {
            instanceService.stop(dataInstance);
        } catch (Exception e) {
            logger.error("stop Instance [name : {}] failure, detail : ",
                         new Object[]{dataInstance.getName(), e});
            responseEntity.setResponseStatus(ResponseEntity.FAILURE);
            responseEntity.setMessage(e.getMessage());
        }
        return responseEntity;
    }

    @RequestMapping("stopWithJson")
    @ResponseBody
    public ResponseEntity stop(@RequestParam("dataInstance") String dataInstanceJson) {
        ResponseEntity responseEntity = new ResponseEntity(ResponseEntity.SUCCESS);
        DataInstance dataInstance = null;
        try {
            dataInstance = JSON.parseObject(dataInstanceJson, DataInstance.class);
            instanceService.stop(dataInstance);
        } catch (Exception e) {
            logger.error("stop Instance [name : {}] failure, detail : ",
                         new Object[]{dataInstance.getName(), e});
            responseEntity.setResponseStatus(ResponseEntity.FAILURE);
            responseEntity.setMessage(e.getMessage());
        }
        return responseEntity;
    }


}
