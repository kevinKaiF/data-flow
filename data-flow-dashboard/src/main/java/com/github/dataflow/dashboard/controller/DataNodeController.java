package com.github.dataflow.dashboard.controller;

import com.github.dataflow.common.model.ResponseEntity;
import com.github.dataflow.common.utils.Constants;
import com.github.dataflow.dashboard.service.DataInstanceService;
import com.github.dataflow.dashboard.service.DataLogService;
import com.github.dataflow.dashboard.utils.HttpUtil;
import com.github.dataflow.dashboard.zookeeper.ZookeeperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.dataflow.dashboard.utils.Constants.NODE_INSTANCE_DETAIL;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/26
 */
@Controller
@RequestMapping("/dataNode")
public class DataNodeController extends BaseController{
    @Autowired
    private ZookeeperClient zookeeperClient;

    @Autowired
    private DataInstanceService dataInstanceService;

    @Autowired
    private DataLogService dataLogService;

    @RequestMapping("/")
    public String index() {
        return "dataNode";
    }

    @RequestMapping("list")
    @ResponseBody
    public ResponseEntity list() {
        return getResponseEntity("list", new Callable() {
            @Override
            public void call(ResponseEntity responseEntity) {
                Map<String, List<String>> nodeMap = new HashMap<>();
                nodeMap.put("producer", getNodes(Constants.DEFAULT_PRODUCER_NODE_PATH));
                nodeMap.put("consumer", getNodes(Constants.DEFAULT_CONSUMER_NODE_PATH));
                responseEntity.setResult(nodeMap);
            }
        });
    }

    private List<String> getNodes(String nodeParentPath) {
        List<String> nodePathList = new ArrayList<String>();
        if (zookeeperClient.exists(nodeParentPath)) {
            List<String> children = zookeeperClient.getChildren(nodeParentPath);
            for (String child : children) {
                nodePathList.add(nodeParentPath + "/" + child);
            }

        }

        return nodePathList;
    }

    @RequestMapping("instanceList")
    @ResponseBody
    public Object listInstance(@RequestParam final String nodePath) {
        return getResponseEntity("listInstance", new Callable(nodePath) {
            @Override
            public void call(ResponseEntity responseEntity) {
                Map dataInstances = dataInstanceService.getDataInstanceByNodePath(nodePath);
                responseEntity.setResult(dataInstances);
            }
        }).getResult();
    }

    @RequestMapping("instanceDetail")
    @ResponseBody
    public ResponseEntity instanceDetail(@RequestParam final String nodeAddress, @RequestParam final String instanceName) {
        return getResponseEntity("instanceDetail", new Callable(nodeAddress, instanceName) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.clone(HttpUtil.post(buildInstanceDetailUrl(nodeAddress), com.github.dataflow.dashboard.utils.Constants.INSTANCE_NAME_KEY, instanceName));
            }
        });
    }

    private String buildInstanceDetailUrl(String address) {
        return "http://" + address + "/" + NODE_INSTANCE_DETAIL;
    }

    @RequestMapping("logList")
    @ResponseBody
    public Object logList(@RequestParam final String instanceName,
                          @RequestParam(value = "start", required = false) final Integer start,
                          @RequestParam(value = "length", required = false) final Integer length) {
        return getResponseEntity("logList", new Callable(instanceName, start, length) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataLogService.getDataLogListByInstanceName(instanceName, start, length));
            }
        }).getResult();
    }

    @RequestMapping("logDetail")
    @ResponseBody
    public ResponseEntity logDetail(@RequestParam("logId") final Long logId) {
        return getResponseEntity("logDetail", new Callable(logId) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataLogService.getLogById(logId));
            }
        });
    }

}
