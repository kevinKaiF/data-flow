package com.github.dataflow.core.store;

import com.github.dataflow.common.model.DataFlowLifeCycle;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.core.transformer.DataTransformer;
import com.github.dataflow.sender.core.DataSender;

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

    void setDataTransformer(DataTransformer dataTransformer);

    void setDataSenderMap(Map<String, DataSender> dataSenderMap);

    void setColumnsToFilterMap(Map<String, Map<String, List<String>>> columnsToFilterMap);

    void setInstanceName(String instanceName);
}
