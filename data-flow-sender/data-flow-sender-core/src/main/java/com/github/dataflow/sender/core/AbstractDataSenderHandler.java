package com.github.dataflow.sender.core;


import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.dubbo.model.DataOutputMapping;

import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/19
 */
public abstract class AbstractDataSenderHandler implements DataSenderHandler {
    @Override
    public DataSender doCreateDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        DataSender dataSender = createDataSender(dataOutputMapping);
        afterCreateDataSender(dataSender, dataOutputMapping);
        return dataSender;
    }

    protected void afterCreateDataSender(DataSender dataSender, DataOutputMapping dataSourceOutput) {
    }

    protected abstract DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception;

    protected Properties parseToProperties(String options) {
        return JSONObject.parseObject(options, Properties.class);
    }
}
