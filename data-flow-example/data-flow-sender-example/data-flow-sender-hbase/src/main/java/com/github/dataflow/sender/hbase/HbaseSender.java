package com.github.dataflow.sender.hbase;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.event.EventHandler;
import com.github.dataflow.sender.database.DatabaseDataSender;
import com.github.dataflow.sender.database.handler.AbstractDatabaseDataSenderHandler;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.util.List;

/**
 * @author kevin
 * @date 2017-08-27 6:13 PM.
 */
public class HbaseSender extends DatabaseDataSender {
    private DataSourceType dataSourceType = DataSourceType.HBASE;

    @Override
    protected boolean supportSingleSend(Exception e) {
        return true;
    }

    @Override
    protected boolean isSupport(RowMetaData rowMetaData, EventHandler eventHandler) {
        return eventHandler.support(dataSourceType, rowMetaData.getEventType());
    }
}
