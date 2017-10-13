package com.github.dataflow.sender.hbase.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.EventDataSender;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.event.handler.EventHandler;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.sender.core.handler.EventDataSenderHandler;
import com.github.dataflow.sender.hbase.HbaseSender;
import com.github.dataflow.sender.hbase.config.HbaseConfig;
import com.github.dataflow.sender.hbase.event.handler.AbstractHbaseEventHandler;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * @author kevin
 * @date 2017-08-27 6:04 PM.
 */
public class HbaseDataSenderHandler extends EventDataSenderHandler {
    private DataSourceType dataSourceType = DataSourceType.HBASE;

    @Override
    public boolean support(int type) {
        return dataSourceType.getType() == type;
    }

    @Override
    protected DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        HbaseSender hbaseSender = new HbaseSender();
        JSONObject props = JSONObjectUtil.parseJSON(dataOutputMapping.getDataSourceOutput().getOptions());
        hbaseSender.setDataSourceHolder(new DataSourceHolder(getDataSource(props)));
        return hbaseSender;
    }

    protected Object getDataSource(JSONObject props) {
        Connection connection = null;
        try {
            Configuration configuration = HBaseConfiguration.create();
            configuration.set(HbaseConfig.ZOOKEEPER_CLIENT_PORT, JSONObjectUtil.getString(props, HbaseConfig.ZOOKEEPER_CLIENT_PORT, "2181"));
            configuration.set(HbaseConfig.ZOOKEEPER_QUORUM, JSONObjectUtil.getString(props, HbaseConfig.ZOOKEEPER_QUORUM));
            configuration.set(HbaseConfig.HBASE_MASTER, JSONObjectUtil.getString(props, HbaseConfig.HBASE_MASTER));
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            throw new DataSenderException(e);
        }
        return connection;
    }

    @Override
    protected JSONObject refreshDataOutputMapping(DataOutputMapping dataOutputMapping) {
        return null;
    }

    @Override
    protected void afterCreateDataSender(DataSender dataSender, DataOutputMapping dataOutputMapping) {
        super.afterCreateDataSender(dataSender, dataOutputMapping);
        // set batch
        JSONObject properties = JSONObjectUtil.parseJSON(dataOutputMapping.getDataSourceOutput().getOptions());
        EventDataSender eventDataSender = (EventDataSender) dataSender;
        eventDataSender.setBatch(JSONObjectUtil.getBoolean(properties, HbaseConfig.BATCH, Boolean.TRUE));
    }

    @Override
    protected Class<? extends EventHandler> getSupportedEventHandler() {
        return AbstractHbaseEventHandler.class;
    }
}
