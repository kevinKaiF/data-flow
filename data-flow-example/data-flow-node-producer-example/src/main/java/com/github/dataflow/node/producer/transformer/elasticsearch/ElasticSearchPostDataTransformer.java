package com.github.dataflow.node.producer.transformer.elasticsearch;

import com.floragunn.searchguard.ssl.SearchGuardSSLPlugin;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.transformer.core.post.PostDataTransformer;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Repository;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Properties;

/**
 * 如果想在transformer中定义自定义处理逻辑，比如数据的转换，发送等等，
 * 当使用Void泛型，返回值类型，
 *
 * 这里以elasticSearch为例
 * 总结：elasticSerach,Hbase这种类型，字段是动态，或者字段很长，
 * 不可能一张数据库表对应elasticSearch的一个type或者Hbase的一个字段，适合用这种方式进行处理
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/10/18
 */
@Repository
public class ElasticSearchPostDataTransformer implements PostDataTransformer<Void>, InitializingBean{
    private TransportClient transportClient;
    @Override
    public Void transform(List<RowMetaData> rowMetaDataList) {
        // 自定义处理逻辑
        for (RowMetaData rowMetaData : rowMetaDataList) {
            switch (rowMetaData.getEventType()) {
                case INSERT:
                    // 处理插入逻辑
                    break;
                case UPDATE:
                    // 处理更新逻辑
                    break;
                case DELETE:
                    // 处理删除逻辑
                    break;
            }
        }
        return RETURN_VOID;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.transportClient = getTransportClient();
    }

    private TransportClient getTransportClient() {
        TransportClient transportClient = null;
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("elasticsearch.properties"));
            boolean searchGuardEnable = PropertyUtil.getBoolean(properties, ElasticsearchConfig.SEARCHGUARD_ENABLE, "false");
            if (searchGuardEnable) {
                Settings settings = Settings.settingsBuilder()
                        .put(ElasticsearchConfig.CLUSTER_NAME, PropertyUtil.getString(properties, ElasticsearchConfig.CLUSTER_NAME))
                        .put(ElasticsearchConfig.CLIENT_TRANSPORT_SNIFF, PropertyUtil.getBoolean(properties, ElasticsearchConfig.CLIENT_TRANSPORT_SNIFF, "true"))
                        .put(ElasticsearchConfig.PATH_HOME, PropertyUtil.getString(properties, ElasticsearchConfig.PATH_HOME, "."))
                        .put(ElasticsearchConfig.PATH_CONF, PropertyUtil.getString(properties, ElasticsearchConfig.PATH_CONF))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENABLED, PropertyUtil.getBoolean(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENABLED, "true"))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_FILEPATH, PropertyUtil.getString(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_FILEPATH))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_FILEPATH, PropertyUtil.getString(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_FILEPATH))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION, PropertyUtil.getBoolean(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION, "false"))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENABLE_OPENSSL_IF_AVAILABLE, PropertyUtil.getBoolean(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_ENABLE_OPENSSL_IF_AVAILABLE, "false"))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_PASSWORD, PropertyUtil.getString(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_PASSWORD))
                        .put(ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_PASSWORD, PropertyUtil.getString(properties, ElasticsearchConfig.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_PASSWORD))
                        .build();
                transportClient = TransportClient.builder().settings(settings).addPlugin(SearchGuardSSLPlugin.class).build();
            } else {
                Settings settings = Settings.settingsBuilder()
                        .put(ElasticsearchConfig.CLUSTER_NAME, PropertyUtil.getString(properties, ElasticsearchConfig.CLUSTER_NAME))
                        .put(ElasticsearchConfig.CLIENT_TRANSPORT_SNIFF, PropertyUtil.getBoolean(properties, ElasticsearchConfig.CLIENT_TRANSPORT_SNIFF, "true"))
                        .build();
                transportClient = TransportClient.builder().settings(settings).build();
            }

            String[] hosts = PropertyUtil.getString(properties, ElasticsearchConfig.CLUSTER_HOST).split(",");
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
}
