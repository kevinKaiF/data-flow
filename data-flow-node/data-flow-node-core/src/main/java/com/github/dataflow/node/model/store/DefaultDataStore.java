package com.github.dataflow.node.model.store;

import com.github.dataflow.common.model.AbstractDataFlowLifeCycle;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.node.exception.DataStoreHandleException;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.transformer.core.pre.PreDataTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 数据处理器
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/5/30
 */
public class DefaultDataStore extends AbstractDataFlowLifeCycle implements DataStore {
    private static Logger logger = LoggerFactory.getLogger(DefaultDataStore.class);

    /**
     * DataOutputMapping配置schemaName为*，表示匹配所有库
     */
    private String GLOBAL_SCHEMA_NAME = "*";

    /**
     * 数据转换
     */
    protected PreDataTransformer dataTransformer;

    /**
     * schema映射一个DataSender
     * <p>
     * note:本想设计成一个schema映射多个DataSender，但是会引发多个DataSender的事务问题。
     * 比如DataSender A 发送成功，但是DataSender B发送失败，这种情况就很难处理。所以设
     * 计成一个schema映射一个DataSender，一旦发送失败，直接抛出异常即可，不用考虑事务问
     * 题。
     */
    protected Map<String, DataSender> dataSenderMap;

    /**
     * 需要过滤的字段
     * <p>
     * key是schema.name格式，value设计成map，方便快速查找
     */
    protected Map<String, Map<String, Map<String, Boolean>>> columnsToFilterMap;

    /**
     * instance的名称
     */
    protected String instanceName;

    /**
     * 处理数据
     *
     * @param rowMetaDataList
     */
    public void handle(List<RowMetaData> rowMetaDataList) {
        try {
            // 转换
            List<RowMetaData> transformedRowMetaDataList = transformData(rowMetaDataList);
            // 过滤字段
            List<RowMetaData> filteredRowMetaDataList = filterData(transformedRowMetaDataList);
            for (RowMetaData rowMetaData : filteredRowMetaDataList) {
                // 处理schemaName为*的情况
                DataSender dataSender = dataSenderMap.get(GLOBAL_SCHEMA_NAME);

                if (dataSender == null) {
                    dataSender = dataSenderMap.get(rowMetaData.getSchemaName());
                }

                if (dataSender == null) {
                    logger.warn("No DataSender found for the schema [{}], so discard the RowMetaData.", rowMetaData.getSchemaName());
                } else {
                    dataSender.send(transformedRowMetaDataList);
                    logger.debug("DataSender[id : {}] send the RowMetaData successfully. RowMetaData : {} ", dataSender.getDataSenderId(), transformedRowMetaDataList);
                }
            }
        } catch (Exception e) {
            throw new DataStoreHandleException(e);
        }
    }

    protected List<RowMetaData> filterData(List<RowMetaData> rowMetaDataList) {
        if (!CollectionUtils.isEmpty(columnsToFilterMap)) {
            for (RowMetaData rowMetaData : rowMetaDataList) {
                // 暂不考虑schema,table大小写的问题
                String fullTableName = rowMetaData.getSchemaName() + "." + rowMetaData.getTableName();
                Map<String, Map<String, Boolean>> columnNamesToFilter = columnsToFilterMap.get(fullTableName);
                if (!CollectionUtils.isEmpty(columnNamesToFilter)) {
                    Map<String, Boolean> columnMap = columnNamesToFilter.get(rowMetaData.getTableName());
                    if (!CollectionUtils.isEmpty(columnMap)) {
                        filterColumn(columnMap, rowMetaData.getBeforeColumns());
                        filterColumn(columnMap, rowMetaData.getAfterColumns());
                    }
                }
            }
        }
        return rowMetaDataList;
    }

    private void filterColumn(Map<String, Boolean> columnMap, List<RowMetaData.ColumnMeta> columns) {
        if (!CollectionUtils.isEmpty(columns)) {
            for (int i = 0; i < columns.size(); ) {
                if (columnMap != null && columnMap.get(columns.get(i).getColumnName()) != null) {
                    i++;
                } else {
                    columns.remove(i);
                }
            }
        }
    }

    @Override
    public void start() {
        super.start();
        startDataSenders();
    }

    private void startDataSenders() {
        for (DataSender dataSender : dataSenderMap.values()) {
            dataSender.start();
        }
    }

    @Override
    public void stop() {
        super.stop();
        stopDataSenders();
        clearColumnsToFilterMap();
    }

    private void clearColumnsToFilterMap() {
        if (columnsToFilterMap != null) {
            columnsToFilterMap.clear();
        }
    }

    private void stopDataSenders() {
        if (dataSenderMap != null) {
            for (DataSender dataSender : dataSenderMap.values()) {
                dataSender.stop();
            }
        }
    }

    protected List<RowMetaData> transformData(List<RowMetaData> rowMetaDataList) {
        List<RowMetaData> transformedRowMetaData = rowMetaDataList;
        if (dataTransformer != null) {
            transformedRowMetaData = dataTransformer.transform(rowMetaDataList);
        }
        return transformedRowMetaData;
    }

    public PreDataTransformer getDataTransformer() {
        return dataTransformer;
    }

    @Override
    public void setDataTransformer(PreDataTransformer dataTransformer) {
        this.dataTransformer = dataTransformer;
    }

    public Map<String, DataSender> getDataSenderMap() {
        return dataSenderMap;
    }

    @Override
    public void setDataSenderMap(Map<String, DataSender> dataSenderMap) {
        this.dataSenderMap = dataSenderMap;
    }

    public Map<String, Map<String, Map<String, Boolean>>> getColumnsToFilterMap() {
        return columnsToFilterMap;
    }

    public void setColumnsToFilterMap(Map<String, Map<String, Map<String, Boolean>>> columnsToFilterMap) {
        this.columnsToFilterMap = columnsToFilterMap;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
