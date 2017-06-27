package com.github.dataflow.dashboard.controller;

import com.github.dataflow.common.model.ResponseEntity;
import com.github.dataflow.dashboard.service.DataSourceOutputService;
import com.github.dataflow.dubbo.model.DataSourceOutput;
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
 * @date : 2017/6/5
 */
@Controller
@RequestMapping("/dataSourceOutput")
public class DataSourceOutputController extends BaseController {

    @Autowired
    private DataSourceOutputService dataSourceOutputService;

    @RequestMapping
    public String index() {
        return "dataSourceOutput";
    }

    @RequestMapping("list")
    @ResponseBody
    public Object list(final DataSourceOutput dataSourceOutput,
                       @RequestParam(value = "start", required = false) final Integer start,
                       @RequestParam(value = "length", required = false) final Integer length) {
        return getResponseEntity("list", new Callable(dataSourceOutput, start, length) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataSourceOutputService.findByCondition(dataSourceOutput, start, length));
            }
        }).getResult();
    }

    @RequestMapping("add")
    @ResponseBody
    public ResponseEntity add(@Valid final DataSourceOutput dataSourceOutput, BindingResult bindingResult) {
        return getResponseEntity("add", bindingResult, new Callable(dataSourceOutput) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataSourceOutputService.insert(dataSourceOutput));
            }
        });
    }

    @RequestMapping("update")
    @ResponseBody
    public ResponseEntity update(@Valid final DataSourceOutput dataSourceOutput, BindingResult bindingResult) {
        return getResponseEntity("update", bindingResult, new Callable(dataSourceOutput) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataSourceOutputService.insert(dataSourceOutput));
            }
        });
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResponseEntity delete(@RequestParam("id") final Long id) {
        return getResponseEntity("delete", new Callable(id) {
            @Override
            public void call(ResponseEntity responseEntity) {
                dataSourceOutputService.delete(id);
            }
        });
    }
}
