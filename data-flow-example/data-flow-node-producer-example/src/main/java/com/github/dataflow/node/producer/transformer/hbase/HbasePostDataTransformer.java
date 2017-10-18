package com.github.dataflow.node.producer.transformer.hbase;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.transformer.core.post.PostDataTransformer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/10/18
 */
@Repository
public class HbasePostDataTransformer implements PostDataTransformer<Void>, InitializingBean {
    private Connection connection;
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
        this.connection = getConnection();
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("hbase.properties"));
            Configuration configuration = HBaseConfiguration.create();
            configuration.set(HbaseConfig.ZOOKEEPER_CLIENT_PORT, PropertyUtil.getString(props, HbaseConfig.ZOOKEEPER_CLIENT_PORT, "2181"));
            configuration.set(HbaseConfig.ZOOKEEPER_QUORUM, PropertyUtil.getString(props, HbaseConfig.ZOOKEEPER_QUORUM));
            configuration.set(HbaseConfig.HBASE_MASTER, PropertyUtil.getString(props, HbaseConfig.HBASE_MASTER));
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            throw new DataSenderException(e);
        }
        return connection;
    }
}
