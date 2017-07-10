package com.github.dataflow.sender.database.handler;

import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.event.EventHandler;
import com.github.dataflow.sender.core.handler.AbstractDataSenderHandler;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.sender.database.DatabaseDataSender;
import com.github.dataflow.sender.database.config.DatabaseConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/23
 */
public abstract class AbstractDatabaseDataSenderHandler extends AbstractDataSenderHandler implements ApplicationContextAware {
    protected static final List<DataSourceType> DATA_SOURCE_TYPES = new ArrayList<>();

    static {
        DATA_SOURCE_TYPES.add(DataSourceType.MYSQL);
        DATA_SOURCE_TYPES.add(DataSourceType.ORACLE);
        DATA_SOURCE_TYPES.add(DataSourceType.POSTGRESQL);
        DATA_SOURCE_TYPES.add(DataSourceType.SQLSERVER);
        DATA_SOURCE_TYPES.add(DataSourceType.HIVE);
    }

    protected List<EventHandler> eventHandlers = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, EventHandler> eventHandlerMap = applicationContext.getBeansOfType(EventHandler.class);
        if (CollectionUtils.isEmpty(eventHandlers)) {
            throw new DataSenderException("there is no EventHandler bean");
        } else {
            eventHandlers.clear();
            eventHandlers.addAll(eventHandlerMap.values());
        }
    }

    @Override
    public boolean support(int type) {
        return DATA_SOURCE_TYPES.contains(DataSourceType.parse(type));
    }

    @Override
    protected Properties refreshDataOutputMapping(DataOutputMapping dataOutputMapping) {
        return null;
    }

    @Override
    protected void afterCreateDataSender(DataSender dataSender, DataOutputMapping dataOutputMapping) {
        super.afterCreateDataSender(dataSender, dataOutputMapping);
        // set batch
        Properties properties = parseToProperties(dataOutputMapping.getOptions());
        DatabaseDataSender databaseDataSender = (DatabaseDataSender) dataSender;
        databaseDataSender.setBatch(PropertyUtil.getBoolean(properties, DatabaseConfig.BATCH, "true"));
        // set eventHandlers
        databaseDataSender.setEventHandlers(eventHandlers);
    }
}
