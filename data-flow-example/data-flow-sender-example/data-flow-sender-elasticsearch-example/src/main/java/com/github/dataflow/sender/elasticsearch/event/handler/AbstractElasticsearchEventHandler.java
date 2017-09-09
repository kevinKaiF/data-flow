package com.github.dataflow.sender.elasticsearch.event.handler;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.event.handler.EventHandler;
import com.github.dataflow.transformer.core.DataTransformer;
import org.elasticsearch.client.transport.TransportClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author kevin
 * @date 2017-09-05 11:05 PM.
 */
public abstract class AbstractElasticsearchEventHandler implements EventHandler {
    protected DataSourceType dataSourceType = DataSourceType.ELASTICSEARCH;

    protected static final String INDEX = "index";

    protected static final String TYPE = "type";

    protected static final String ID = "id";

    @Override
    public void singleHandle(DataSourceHolder dataSourceHolder, RowMetaData rowMetaData) throws Exception {
        TransportClient transportClient = (TransportClient) dataSourceHolder.getDataSource();
        doSingleHandle(transportClient, rowMetaData);
    }

    protected abstract void doSingleHandle(TransportClient transportClient, RowMetaData rowMetaData) throws ExecutionException, InterruptedException;

    @Override
    public void batchHandle(DataSourceHolder dataSourceHolder, List<RowMetaData> rowMetaDataList) throws Exception {
        TransportClient transportClient = (TransportClient) dataSourceHolder.getDataSource();
        doBatchHandle(transportClient, rowMetaDataList);
    }

    protected abstract void doBatchHandle(TransportClient transportClient, List<RowMetaData> rowMetaDataList);

    /**
     * 每个表是如何与elasticsearch对应的，可以从{@link DataTransformer#transform(List)}中进行配置
     * 可以在参数list {@link RowMetaData#options} 设置每张表对应的index, type, id
     * <p>
     * 注意id的生成：
     * 1.数据库中id是否与elasticsearch保持一致？
     * 2.主键有多个
     * 3.无主键
     *
     * @param rowMetaData
     * @return
     */
    protected Map<String, String> getElasticsearchInfo(RowMetaData rowMetaData) {
        Map<String, String> elasticsearchInfo = new HashMap<>();
        Map<String, Object> options = rowMetaData.getOptions();
        elasticsearchInfo.put(INDEX, String.valueOf(options.get(INDEX)));
        elasticsearchInfo.put(TYPE, String.valueOf(options.get(TYPE)));
        elasticsearchInfo.put(ID, String.valueOf(options.get(ID)));
        return elasticsearchInfo;
    }
}
