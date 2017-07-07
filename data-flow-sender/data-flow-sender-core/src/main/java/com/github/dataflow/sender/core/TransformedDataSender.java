package com.github.dataflow.sender.core;

import com.github.dataflow.common.model.RowMetaData;

import java.util.List;

/**
 * 需要transform的DataSender
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/6
 */
public abstract class TransformedDataSender extends DataSender{
    @Override
    public void send(List<RowMetaData> rowMetaDataList) throws Exception {
        String transformedValue = dataTransformer.transform(rowMetaDataList);
        doSend(transformedValue);
    }

    protected abstract void doSend(String transformedValue) throws Exception;
}
