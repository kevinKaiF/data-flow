package com.github.dataflow.sender.elasticsearch.handler;

import com.alibaba.fastjson.JSONObject;
import com.floragunn.searchguard.ssl.SearchGuardSSLPlugin;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.event.handler.EventHandler;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.sender.core.handler.EventDataSenderHandler;
import com.github.dataflow.sender.elasticsearch.ElasticsearchSender;
import com.github.dataflow.sender.elasticsearch.config.ElasticsearchConfig;
import com.github.dataflow.sender.elasticsearch.event.handler.AbstractElasticsearchEventHandler;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetSocketAddress;

/**
 * @author kevin
 * @date 2017-09-05 10:37 PM.
 */
public class ElasticsearchDataSenderHandler extends EventDataSenderHandler {
    private DataSourceType dataSourceType = DataSourceType.ELASTICSEARCH;

    @Override
    public boolean support(int type) {
        return dataSourceType.getType() == type;
    }

    @Override
    protected DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        ElasticsearchSender elasticsearchSender = new ElasticsearchSender();
        JSONObject props = JSONObjectUtil.parseJSON(dataOutputMapping.getDataSourceOutput().getOptions());
        elasticsearchSender.setDataSourceHolder(new DataSourceHolder(getDataSource(props)));
        return elasticsearchSender;
    }


    protected Object getDataSource(JSONObject properties) {
        TransportClient transportClient = null;
        try {
            boolean searchGuardEnable = JSONObjectUtil.getBoolean(properties, ElasticsearchConfig.SEARCHGUARD_ENABLE, Boolean.FALSE);
            if (searchGuardEnable) {
                Settings settings = Settings.settingsBuilder()
                        .put(ElasticsearchConfig.CLUSTER_NAME, JSONObjectUtil.getString(properties, ElasticsearchConfig.CLUSTER_NAME))
                        .put(ElasticsearchConfig.CLIENT_TRANSPORT_SNIFF, JSONObjectUtil.getBoolean(properties, ElasticsearchConfig.CLIENT_TRANSPORT_SNIFF, Boolean.TRUE))
                        .put(ElasticsearchConfig.PATH_HOME, JSONObjectUtil.getString(properties, ElasticsearchConfig.PATH_HOME, "."))
                        .put(ElasticsearchConfig.PATH_CONF, JSONObjectUtil.getString(properties, ElasticsearchConfig.PATH_CONF))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENABLED, JSONObjectUtil.getBoolean(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENABLED, Boolean.TRUE))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_FILEPATH, JSONObjectUtil.getString(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_FILEPATH))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_FILEPATH, JSONObjectUtil.getString(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_FILEPATH))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION, JSONObjectUtil.getBoolean(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION, Boolean.FALSE))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENABLE_OPENSSL_IF_AVAILABLE, JSONObjectUtil.getBoolean(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENABLE_OPENSSL_IF_AVAILABLE, Boolean.FALSE))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_PASSWORD, JSONObjectUtil.getString(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_PASSWORD))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_PASSWORD, JSONObjectUtil.getString(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_PASSWORD))
                        .build();
                transportClient = TransportClient.builder().settings(settings).addPlugin(SearchGuardSSLPlugin.class).build();
            } else {
                Settings settings = Settings.settingsBuilder()
                        .put(ElasticsearchConfig.CLUSTER_NAME, JSONObjectUtil.getString(properties, ElasticsearchConfig.CLUSTER_NAME))
                        .put(ElasticsearchConfig.CLIENT_TRANSPORT_SNIFF, JSONObjectUtil.getBoolean(properties, ElasticsearchConfig.CLIENT_TRANSPORT_SNIFF, Boolean.TRUE))
                        .build();
                transportClient = TransportClient.builder().settings(settings).build();
            }

            String[] hosts = JSONObjectUtil.getString(properties, ElasticsearchConfig.CLUSTER_HOST).split(",");
            for (String host : hosts) {
                if (host.contains(":")) {
                    String ip = host.split(":")[0];
                    int port = Integer.parseInt(host.split(":")[1]);
                    transportClient.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(ip, port)));
                } else {
                    transportClient.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(host, 9300)));
                }
            }
        } catch (Exception e) {
            throw new DataSenderException(e);
        }
        return transportClient;
    }

    @Override
    protected JSONObject refreshDataOutputMapping(DataOutputMapping dataOutputMapping) {
        return null;
    }

    @Override
    protected Class<? extends EventHandler> getSupportedEventHandler() {
        return AbstractElasticsearchEventHandler.class;
    }
}
