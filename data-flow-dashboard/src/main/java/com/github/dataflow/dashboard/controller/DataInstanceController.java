package com.github.dataflow.dashboard.controller;

import com.github.dataflow.common.model.ResponseEntity;
import com.github.dataflow.dashboard.router.Router;
import com.github.dataflow.dashboard.service.DataInstanceService;
import com.github.dataflow.dashboard.service.DataOutputMappingService;
import com.github.dataflow.dashboard.service.DataTableService;
import com.github.dataflow.dashboard.utils.Constants;
import com.github.dataflow.dashboard.utils.HttpUtil;
import com.github.dataflow.dubbo.common.enums.DataInstanceStatus;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.dubbo.model.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Map;

import static com.github.dataflow.dashboard.utils.Constants.DATA_INSTANCE_KEY;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/4
 */
@Controller
@RequestMapping("/dataInstance")
public class DataInstanceController extends BaseController {
    @Autowired
    private DataInstanceService dataInstanceService;

    @Autowired
    private DataTableService dataTableService;

    @Autowired
    private DataOutputMappingService dataOutputMappingService;

    @Autowired
    private Router router;

    @RequestMapping("producer")
    public String producer() {
        return "dataInstanceProducer";
    }

    @RequestMapping("consumer")
    public String consumer() {
        return "dataInstanceConsumer";
    }

    @RequestMapping("start")
    @ResponseBody
    public ResponseEntity start(@RequestParam final Long id) {
        return getResponseEntity("start", new Callable(id) {
            @Override
            public void call(ResponseEntity responseEntity) {
                DataInstance dataInstance = dataInstanceService.getByIdFully(id);
                if (dataInstance == null) {
                    responseEntity.setResponseStatus(ResponseEntity.FAILURE);
                    responseEntity.setMessage("DataInstance不存在");
                } else {
                    if (isStopOrCreated(dataInstance)) {
                        // 选取节点
                        String address = null;
                        if (dataInstance.getProducerOrConsumer() == 0) {
                            address = router.nextProducer(dataInstance.getName());
                        } else {
                            address = router.nextConsumer(dataInstance.getName());
                        }
                        if (address == null) {
                            responseEntity.setResponseStatus(ResponseEntity.FAILURE);
                            responseEntity.setMessage("没有启动的Node");
                        } else {
                            // http请求
                            responseEntity.clone(HttpUtil.post(buildStartUrl(address), DATA_INSTANCE_KEY, dataInstance));
                        }
                    } else {
                        responseEntity.setResponseStatus(ResponseEntity.FAILURE);
                        responseEntity.setMessage("DataInstance必须是STOP或者CREATED");
                    }
                }
            }
        });
    }

    private String buildStartUrl(String address) {
        return "http://" + address + Constants.START_WITH_JSON_URI;
    }

    private boolean isStopOrCreated(DataInstance dataInstance) {
        return dataInstance.getStatus() == DataInstanceStatus.STOP.getStatus() || dataInstance.getStatus() == DataInstanceStatus.CREATED.getStatus();
    }

    @RequestMapping("stop")
    @ResponseBody
    public ResponseEntity stop(@RequestParam final Long id) {
        return getResponseEntity("stop", new Callable() {
            @Override
            public void call(ResponseEntity responseEntity) {
                DataInstance dataInstance = dataInstanceService.getById(id);
                if (dataInstance == null) {
                    responseEntity.setResponseStatus(ResponseEntity.FAILURE);
                    responseEntity.setMessage("DataInstance不存在");
                } else {
                    if (isStart(dataInstance)) {
                        String nodePath = dataInstance.getNodePath();
                        if (StringUtils.isEmpty(nodePath)) {
                            responseEntity.setResponseStatus(ResponseEntity.FAILURE);
                            responseEntity.setMessage("DataInstance没有对应的node");
                        } else {
                            String address = nodePath.substring(nodePath.lastIndexOf("/") + 1);
                            if (address == null) {
                                responseEntity.setResponseStatus(ResponseEntity.FAILURE);
                                responseEntity.setMessage("没有启动的Node");
                            } else {
                                // http请求
                                responseEntity.clone(HttpUtil.post(buildStopUrl(address), Constants.DATA_INSTANCE_KEY, dataInstance));
                            }
                        }
                    } else {
                        responseEntity.setResponseStatus(ResponseEntity.FAILURE);
                        responseEntity.setMessage("DataInstance尚未START");
                    }
                }
            }
        });
    }

    private String buildStopUrl(String address) {
        return "http://" + address + Constants.STOP_WITH_JSON_URI;
    }

    private boolean isStart(DataInstance dataInstance) {
        return dataInstance.getStatus() == DataInstanceStatus.START.getStatus();
    }

    @RequestMapping("add")
    @ResponseBody
    public ResponseEntity add(@Valid final DataInstance dataInstance, BindingResult bindingResult) {
        return getResponseEntity("add", bindingResult, new Callable(dataInstance) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataInstanceService.insert(dataInstance));
            }
        });
    }

    @RequestMapping("update")
    @ResponseBody
    public ResponseEntity update(@Valid final DataInstance dataInstance, BindingResult bindingResult) {
        return getResponseEntity("update", bindingResult, new Callable(dataInstance) {
            @Override
            public void call(ResponseEntity responseEntity) {
                dataInstanceService.update(dataInstance);
            }
        });
    }

    @RequestMapping("list")
    @ResponseBody
    public Object list(final DataInstance dataInstance,
                       @RequestParam(value = "start", required = false) final Integer start,
                       @RequestParam(value = "length", required = false) final Integer length) {
        return getResponseEntity("list", new Callable(dataInstance, start, length) {
            @Override
            public void call(ResponseEntity responseEntity) {
                Map dataInstanceMap = dataInstanceService.findByCondition(dataInstance, start, length);
                responseEntity.setResult(dataInstanceMap);
            }
        }).getResult();
    }

    @RequestMapping("get")
    @ResponseBody
    public ResponseEntity get(@RequestParam final Long id) {
        return getResponseEntity("get", new Callable(id) {
            @Override
            public void call(ResponseEntity responseEntity) {
                DataInstance dataInstance = dataInstanceService.getByIdFully(id);
                responseEntity.setResult(dataInstance);
            }
        });
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResponseEntity delete(@RequestParam("ids[]") final Long[] ids) {
        return getResponseEntity("delete", new Callable(ids) {
            @Override
            public void call(ResponseEntity responseEntity) {
                dataInstanceService.delete(ids);
            }
        });
    }

    // operate table
    @RequestMapping("schema")
    @ResponseBody
    public ResponseEntity schema(@RequestParam final Long id, @RequestParam final Boolean filter) {
        return getResponseEntity("schema", new Callable(id, filter) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataInstanceService.getSchema(id, filter));
            }
        });
    }

    @RequestMapping("table")
    @ResponseBody
    public ResponseEntity table(@RequestParam final Long id, @RequestParam final String schemaName) {
        return getResponseEntity("table", new Callable(id, schemaName) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataInstanceService.getTable(id, schemaName));
            }
        });
    }

    @RequestMapping("tableDetail")
    @ResponseBody
    public ResponseEntity tableDetail(@RequestParam final Long id, @RequestParam final String schemaName, @RequestParam final String tableName) {
        return getResponseEntity("tableDetail", new Callable(id, schemaName, tableName) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataInstanceService.getTableDetail(id, schemaName, tableName));
            }
        });
    }

    @RequestMapping("addTable")
    @ResponseBody
    public ResponseEntity addDataTable(@Valid final DataTable dataTable, BindingResult bindingResult) {
        return getResponseEntity("addDataTable", bindingResult, new Callable(dataTable) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataTableService.insert(dataTable));
            }
        });
    }

    @RequestMapping("updateTable")
    @ResponseBody
    public ResponseEntity updateTable(@Valid final DataTable dataTable, BindingResult bindingResult) {
        return getResponseEntity("updateTable", bindingResult, new Callable(dataTable) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataTableService.update(dataTable));
            }
        });
    }

    @RequestMapping("deleteTable")
    @ResponseBody
    public ResponseEntity deleteTable(@RequestParam("id") final Long id) {
        return getResponseEntity("deleteTable", new Callable(id) {
            @Override
            public void call(ResponseEntity responseEntity) {
                dataTableService.delete(id);
            }
        });
    }

    // operate DataOutputMapping
    @RequestMapping("addMapping")
    @ResponseBody
    public ResponseEntity addMapping(@Valid final DataOutputMapping dataOutputMapping, BindingResult bindingResult) {
        return getResponseEntity("addMapping", bindingResult, new Callable(dataOutputMapping) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataOutputMappingService.insert(dataOutputMapping));
            }
        });
    }

    @RequestMapping("updateMapping")
    @ResponseBody
    public ResponseEntity updateMapping(@Valid final DataOutputMapping dataOutputMapping, BindingResult bindingResult) {
        return getResponseEntity("updateMapping", bindingResult, new Callable(dataOutputMapping) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataOutputMappingService.update(dataOutputMapping));
            }
        });
    }

    @RequestMapping("listMapping")
    @ResponseBody
    public Object listMapping(final DataOutputMapping dataOutputMapping,
                              @RequestParam(value = "start", required = false) final Integer start,
                              @RequestParam(value = "length", required = false) final Integer length) {
        return getResponseEntity("listMapping", new Callable(dataOutputMapping, start, length) {
            @Override
            public void call(ResponseEntity responseEntity) {
                responseEntity.setResult(dataOutputMappingService.findByCondition(dataOutputMapping, start, length));
            }
        }).getResult();
    }


    @RequestMapping("deleteMapping")
    @ResponseBody
    public ResponseEntity deleteMapping(@RequestParam("id") final Long id) {
        return getResponseEntity("deleteMapping", new Callable(id) {
            @Override
            public void call(ResponseEntity responseEntity) {
                dataOutputMappingService.delete(id);
            }
        });
    }
}
