package com.github.dataflow.node.model.instance.factory;

import com.github.dataflow.core.exception.InstanceException;
import com.github.dataflow.core.instance.Instance;
import com.github.dataflow.core.instance.handler.InstanceHandler;
import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.node.model.instance.InstanceManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kevin
 * @date 2017-05-28 3:12 PM.
 */
@Component
public class InstanceFactory implements InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private List<InstanceHandler> instanceHandlers = new ArrayList<InstanceHandler>();

    public synchronized Instance createInstance(DataInstance dataInstance) {
        if (InstanceManager.get(dataInstance.getName()) != null) {
            throw new InstanceException("Instance [" + dataInstance.getName() + "] has existed, don't repeatedly create.");
        }

        for (InstanceHandler instanceHandler : instanceHandlers) {
            if (instanceHandler.support(dataInstance.getType())) {
                return instanceHandler.doCreateInstance(dataInstance);
            }
        }

        throw new InstanceException("don't support Instance:type [" + dataInstance.getType() + "]");
    }


    public void afterPropertiesSet() throws Exception {
        instanceHandlers.clear();
        Map<String, InstanceHandler> instanceHandlerMap = applicationContext.getBeansOfType(InstanceHandler.class);
        if (CollectionUtils.isEmpty(instanceHandlerMap)) {

        } else {
            instanceHandlers.addAll(instanceHandlerMap.values());
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
