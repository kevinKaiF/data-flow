package com.github.dataflow.dashboard.controller;

import com.github.dataflow.common.model.ResponseEntity;
import com.github.dataflow.dashboard.service.DataNodeConfigurationService;
import com.github.dataflow.dubbo.model.DataNodeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/14
 */
@Controller
@RequestMapping("/dataNodeConfiguration")
public class DataNodeConfigurationController extends BaseController {

    @Autowired
    private DataNodeConfigurationService dataNodeConfigurationService;

    @RequestMapping("/")
    public String index() {
        return "dataNodeConfiguration";
    }

    @RequestMapping("add")
    @ResponseBody
    public ResponseEntity add(@Valid final DataNodeConfiguration dataNodeConfiguration, BindingResult bindingResult) {
        return getResponseEntity("add", new Callable(dataNodeConfiguration) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataNodeConfigurationService.insert(dataNodeConfiguration));
            }
        });
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResponseEntity delete(@RequestParam final Long id) {
        return getResponseEntity("delete", new Callable(id) {
            @Override
            public void call(ResponseEntity responseEntity) {
                dataNodeConfigurationService.delete(id);
            }
        });
    }

    @RequestMapping("get")
    @ResponseBody
    public ResponseEntity get() {
        return getResponseEntity("get", new Callable() {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataNodeConfigurationService.get());
            }
        });
    }
}
