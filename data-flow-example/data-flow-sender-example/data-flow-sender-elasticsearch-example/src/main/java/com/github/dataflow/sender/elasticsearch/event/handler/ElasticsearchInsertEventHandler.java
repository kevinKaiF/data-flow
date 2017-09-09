package com.github.dataflow.sender.elasticsearch.event.handler;

import com.alibaba.fastjson.JSON;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.exception.DataSenderException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author kevin
 * @date 2017-09-05 11:09 PM.
 */
public class ElasticsearchInsertEventHandler extends AbstractElasticsearchEventHandler {
    @Override
    public boolean support(DataSourceType dataSourceType, RowMetaData.EventType eventType) {
        return this.dataSourceType == dataSourceType && eventType == RowMetaData.EventType.INSERT;
    }

    @Override
    protected void doSingleHandle(TransportClient transportClient, RowMetaData rowMetaData) throws ExecutionException, InterruptedException {
        doBatchHandle(transportClient, Collections.singletonList(rowMetaData));
    }

    @Override
    protected void doBatchHandle(TransportClient transportClient, List<RowMetaData> rowMetaDataList) {
        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        Map<String, String> elasticsearchInfo = getElasticsearchInfo(rowMetaDataList.get(0));
        for (RowMetaData rowMetaData : rowMetaDataList) {
            bulkRequest.add(transportClient.prepareIndex(elasticsearchInfo.get(INDEX),
                                                         elasticsearchInfo.get(TYPE),
                                                         elasticsearchInfo.get(ID))
                                    .setSource(JSON.toJSONString(rowMetaData)));
        }
        BulkResponse response = bulkRequest.execute().actionGet();
        if (response.hasFailures()) {
            throw new DataSenderException("batch handle failed");
        }
    }
}
