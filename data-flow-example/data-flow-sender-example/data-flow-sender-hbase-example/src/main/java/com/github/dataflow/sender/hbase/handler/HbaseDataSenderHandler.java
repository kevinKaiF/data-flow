package com.github.dataflow.sender.hbase.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.event.EventHandler;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.sender.core.handler.AbstractDataSenderHandler;
import com.github.dataflow.sender.database.DatabaseDataSender;
import com.github.dataflow.sender.hbase.HbaseSender;
import com.github.dataflow.sender.hbase.config.HbaseConfig;
import com.github.dataflow.sender.hbase.event.handler.AbstractHbaseEventHandler;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author kevin
 * @date 2017-08-27 6:04 PM.
 */
public class HbaseDataSenderHandler extends AbstractDataSenderHandler implements ApplicationContextAware {
    private List<EventHandler> eventHandlers = new ArrayList<>();

    private DataSourceType dataSourceType = DataSourceType.HBASE;

    @Override
    public boolean support(int type) {
        return dataSourceType.getType() == type;
    }

    @Override
    protected DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        HbaseSender hbaseSender = new HbaseSender();
        JSONObject props = parseJSON(dataOutputMapping.getDataSourceOutput().getOptions());
        hbaseSender.setDataSourceHolder(new DataSourceHolder(getHbaseDataSource(props)));
        return hbaseSender;
    }

    private Object getHbaseDataSource(JSONObject props) {
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
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, EventHandler> eventHandlerMap = applicationContext.getBeansOfType(EventHandler.class);
        if (CollectionUtils.isEmpty(eventHandlerMap)) {
            throw new DataSenderException("there is no EventHandler bean");
        } else {
            eventHandlers.clear();
            Collection<EventHandler> eventHandlerToUse = eventHandlerMap.values();
            for (EventHandler eventHandler : eventHandlerToUse) {
                if (eventHandler instanceof AbstractHbaseEventHandler) {
                    eventHandlers.add(eventHandler);
                }
            }

            if (CollectionUtils.isEmpty(eventHandlers)) {
                throw new DataSenderException("there is no EventHandler bean instanceof AbstractHbaseEventHandler");
            }
        }
    }

    @Override
    protected void afterCreateDataSender(DataSender dataSender, DataOutputMapping dataOutputMapping) {
        super.afterCreateDataSender(dataSender, dataOutputMapping);
        // set batch
        JSONObject properties = parseJSON(dataOutputMapping.getOptions());
        DatabaseDataSender databaseDataSender = (DatabaseDataSender) dataSender;
        databaseDataSender.setBatch(JSONObjectUtil.getBoolean(properties, HbaseConfig.BATCH, Boolean.TRUE));
        // set eventHandlers
        databaseDataSender.setEventHandlers(eventHandlers);
    }
}
