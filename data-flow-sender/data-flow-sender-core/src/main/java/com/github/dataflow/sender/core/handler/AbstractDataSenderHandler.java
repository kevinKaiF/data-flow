package com.github.dataflow.sender.core.handler;


import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;

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

    protected void afterCreateDataSender(DataSender dataSender, DataOutputMapping dataOutputMapping) {
        Long dataSenderId = dataOutputMapping.getDataSourceOutput().getId();
        String dataSenderName = dataOutputMapping.getDataSourceOutput().getName();
        dataSender.setDataSenderId(dataSenderId);
        dataSender.setDataSenderName(dataSenderName);
    }

    protected abstract DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception;

    /**
     * 设置DataOutputMapping的options默认值，校验必须属性
     *
     * @param dataOutputMapping
     * @return
     */
    protected abstract JSONObject refreshDataOutputMapping(DataOutputMapping dataOutputMapping);

}
