package com.github.dataflow.node.controller;

import com.github.dataflow.common.model.ResponseEntity;
import com.github.dataflow.core.instance.Instance;
import com.github.dataflow.core.instance.InstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/26
 */

@Controller
@RequestMapping("node")
public class NodeController {
    private Logger logger = LoggerFactory.getLogger(NodeController.class);

    @RequestMapping("instanceDetail")
    @ResponseBody
    public ResponseEntity instanceDetail(@RequestParam String instanceName) {
        ResponseEntity responseEntity = new ResponseEntity();
        try {
            Instance instance = InstanceManager.get(instanceName);
            String position = instance.getPosition(instanceName);
            responseEntity.setResult(position);
        } catch (Exception e) {
            logger.error("get the position of DataInstance [{}] failure, detail : ", instanceName, e);
            responseEntity.setResponseStatus(ResponseEntity.FAILURE);
            responseEntity.setMessage(e.getMessage());
        }
        return responseEntity;
    }
}
