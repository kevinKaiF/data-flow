package com.github.dataflow.sender.local.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.handler.AbstractDataSenderHandler;
import com.github.dataflow.sender.local.LocalSender;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/9/9
 */
public class LocalSenderHandler extends AbstractDataSenderHandler{
    private DataSourceType dataSourceType = DataSourceType.LOCAL;

    @Override
    protected DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        LocalSender localSender = new LocalSender();
        localSender.setProps(JSONObjectUtil.parseJSON(dataOutputMapping.getDataSourceOutput().getOptions()));
        return localSender;
    }

    @Override
    protected JSONObject refreshDataOutputMapping(DataOutputMapping dataOutputMapping) {
        return null;
    }

    @Override
    public boolean support(int type) {
        return dataSourceType.getType() == type;
    }
}
