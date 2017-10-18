package com.github.dataflow.sender.core.handler;

import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.transformer.core.post.JSONDataTransformer;
import com.github.dataflow.transformer.core.post.PostGroovyShellDataTransformer;
import org.springframework.util.StringUtils;

/**
 * 需求transform的DataSenderHandler，主要用于消息类型Sender
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/6
 */
public abstract class TransformedDataSenderHandler extends AbstractDataSenderHandler {
    @Override
    protected void afterCreateDataSender(DataSender dataSender, DataOutputMapping dataOutputMapping) {
        super.afterCreateDataSender(dataSender, dataOutputMapping);
        if (StringUtils.isEmpty(dataOutputMapping.getTransformScript())) {
            if (dataSender.getDataTransformer() == null) {
                dataSender.setDataTransformer(new JSONDataTransformer());
            }
        } else {
            dataSender.setDataTransformer(new PostGroovyShellDataTransformer(dataOutputMapping.getTransformScript()));
        }
    }
}
