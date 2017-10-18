package com.github.dataflow.sender.core.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.dubbo.common.enums.DataSourceType;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.DefaultDataSender;
import com.github.dataflow.sender.core.config.MappingConfig;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.transformer.core.post.PostDataTransformer;
import org.springframework.util.StringUtils;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/10/18
 */
public class DefaultDataSenderHandler extends AbstractDataSenderHandler {
    private DataSourceType dataSourceType = DataSourceType.NONE;

    @Override
    protected DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        return new DefaultDataSender();
    }

    @Override
    protected JSONObject refreshDataOutputMapping(DataOutputMapping dataOutputMapping) {
        return null;
    }

    @Override
    public boolean support(int type) {
        return dataSourceType.getType() == type;
    }

    @Override
    protected void setTransformer(DataSender dataSender, DataOutputMapping dataOutputMapping) {
        JSONObject properties = JSONObjectUtil.parseJSON(dataOutputMapping.getOptions());
        String transformBean = JSONObjectUtil.getString(properties, MappingConfig.POST_DATA_TRANSFORMER_BEAN);
        if (StringUtils.isEmpty(transformBean)) {
            throw new DataSenderException("the property [transformBean] is not configured in DataOutputMapping.Options.");
        } else {
            Object bean = applicationContext.getBean(transformBean);
            if (bean == null) {
                throw new DataSenderException(String.format("transformBean[%s] is not found.", transformBean));
            } else if (bean instanceof PostDataTransformer) {
                dataSender.setDataTransformer((PostDataTransformer) bean);
            } else {
                throw new DataSenderException(String.format("transformBean[%s] is not a instance of DataTransformer.", transformBean));
            }
        }
    }
}
