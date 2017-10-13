package com.github.dataflow.sender.hbase;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.Closer;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.sender.core.EventDataSender;
import com.github.dataflow.sender.core.event.handler.EventHandler;
import org.apache.hadoop.hbase.client.Connection;

/**
 * @author kevin
 * @date 2017-08-27 6:13 PM.
 */
public class HbaseSender extends EventDataSender {
    private DataSourceType dataSourceType = DataSourceType.HBASE;

    @Override
    protected boolean supportSingleSend(Exception e) {
        return true;
    }

    @Override
    protected boolean isSupport(RowMetaData rowMetaData, EventHandler eventHandler) {
        return eventHandler.support(dataSourceType, rowMetaData.getEventType());
    }

    @Override
    protected void doStop() {
        Connection connection = (Connection) dataSourceHolder.getDataSource();
        Closer.closeQuietly(connection);
    }
}
