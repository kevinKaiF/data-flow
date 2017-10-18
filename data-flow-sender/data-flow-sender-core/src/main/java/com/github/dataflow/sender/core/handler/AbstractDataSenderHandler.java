package com.github.dataflow.sender.core.handler;


import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.config.MappingConfig;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.transformer.core.post.PostDataTransformer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/19
 */
public abstract class AbstractDataSenderHandler implements DataSenderHandler, ApplicationContextAware {
    protected ApplicationContext applicationContext;

    @Override
    public DataSender doCreateDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        DataSender dataSender = createDataSender(dataOutputMapping);
        afterCreateDataSender(dataSender, dataOutputMapping);
        return dataSender;
    }

    protected void afterCreateDataSender(DataSender dataSender, DataOutputMapping dataOutputMapping) {
        Long dataSenderId = dataOutputMapping.getDataSourceOutput().getId();
        String dataSenderName = dataOutputMapping.getDataSourceOutput().getName();
        dataSender.setDataSenderId(dataSenderId);
        dataSender.setDataSenderName(dataSenderName);
        // 配置transformer
        setTransformer(dataSender, dataOutputMapping);
    }

    protected void setTransformer(DataSender dataSender, DataOutputMapping dataOutputMapping) {
        JSONObject properties = JSONObjectUtil.parseJSON(dataOutputMapping.getDataSourceOutput().getOptions());
        String transformBean = JSONObjectUtil.getString(properties, MappingConfig.POST_DATA_TRANSFORMER_BEAN);
        if (!StringUtils.isEmpty(transformBean)) {
            Object bean = applicationContext.getBean(transformBean);
            if (bean == null) {
                throw new DataSenderException(String.format("transformBean[%s] is not found.", transformBean));
            } else if (bean instanceof PostDataTransformer) {
                dataSender.setDataTransformer((PostDataTransformer) bean);
            } else {
                throw new DataSenderException(String.format("transformBean[%s] is not a instance of com.github.dataflow.transformer.core.post.PostDataTransformer.", transformBean));
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected abstract DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception;

    /**
     * 设置DataOutputMapping的options默认值，校验必须属性
     *
     * @param dataOutputMapping
     * @return
     */
    protected abstract JSONObject refreshDataOutputMapping(DataOutputMapping dataOutputMapping);

}
