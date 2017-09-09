package com.github.dataflow.sender.elasticsearch.event.handler;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.exception.DataSenderException;
import org.elasticsearch.action.deletebyquery.DeleteByQueryAction;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author kevin
 * @date 2017-09-05 11:10 PM.
 */
public class ElasticsearchDeleteEventHandler extends AbstractElasticsearchEventHandler {
    @Override
    public boolean support(DataSourceType dataSourceType, RowMetaData.EventType eventType) {
        return this.dataSourceType == dataSourceType && eventType == RowMetaData.EventType.DELETE;
    }

    @Override
    protected void doSingleHandle(TransportClient transportClient, RowMetaData rowMetaData) {
        doBatchHandle(transportClient, Collections.singletonList(rowMetaData));
    }

    @Override
    protected void doBatchHandle(TransportClient transportClient, List<RowMetaData> rowMetaDataList) {
        Map<String, String> elasticsearchInfo = getElasticsearchInfo(rowMetaDataList.get(0));
        DeleteByQueryResponse deleteByQueryResponse = new DeleteByQueryRequestBuilder(transportClient, DeleteByQueryAction.INSTANCE)
                .setIndices(elasticsearchInfo.get(INDEX))
                .setTypes(elasticsearchInfo.get(TYPE))
                .setQuery(QueryBuilders.termsQuery("id", elasticsearchInfo.get(ID)))
                .execute()
                .actionGet();

        if (deleteByQueryResponse.getTotalFailed() > 0) {
            throw new DataSenderException("batch handle failed");
        }
    }
}
