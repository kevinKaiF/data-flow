package com.github.dataflow.sender.core.handler;

import com.github.dataflow.sender.core.event.handler.EventHandler;
import com.github.dataflow.sender.core.exception.DataSenderException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 需要EventHandler处理的Sender
 *
 * @author kevin
 * @date 2017-09-05 10:57 PM.
 */
public abstract class EventDataSenderHandler extends AbstractDataSenderHandler implements ApplicationContextAware {
    protected List<EventHandler> eventHandlers = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, EventHandler> eventHandlerMap = applicationContext.getBeansOfType(EventHandler.class);
        if (CollectionUtils.isEmpty(eventHandlerMap)) {
            throw new DataSenderException("there is no EventHandler bean");
        } else {
            eventHandlers.clear();
            Collection<EventHandler> eventHandlerToUse = eventHandlerMap.values();
            Class<? extends EventHandler> supportedEventHandlerClass = getSupportedEventHandler();
            for (EventHandler eventHandler : eventHandlerToUse) {
                if (supportedEventHandlerClass.isInstance(eventHandler)) {
                    eventHandlers.add(eventHandler);
                }
            }

            if (CollectionUtils.isEmpty(eventHandlers)) {
                throw new DataSenderException("there is no EventHandler bean instanceof " + supportedEventHandlerClass.getName());
            }
        }
    }

    /**
     * 获取所支持的EventHandler的class类型
     *
     * @return
     */
    protected abstract Class<? extends EventHandler> getSupportedEventHandler();
}
