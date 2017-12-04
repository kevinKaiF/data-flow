package com.github.dataflow.node.model.store;


import com.github.dataflow.common.model.DataFlowLifeCycle;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.transformer.core.pre.PreDataTransformer;

import java.util.List;
import java.util.Map;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/5
 */
public interface DataStore extends DataFlowLifeCycle {
    void handle(List<RowMetaData> rowMetaDataList);

    void setDataTransformer(PreDataTransformer dataTransformer);

    void setDataSenderMap(Map<String, DataSender> dataSenderMap);

    void setColumnsToFilterMap(Map<String, Map<String, Map<String, Boolean>>> columnsToFilterMap);

    void setInstanceName(String instanceName);
}
