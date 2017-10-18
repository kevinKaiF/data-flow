package com.github.dataflow.node.producer.transformer;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.transformer.core.post.PostDataTransformer;

import java.util.List;

/**
 * transformer的作用可以将拿到的数据转换成自定义的处理，可以在创建实例的第三步，
 * “输出映射新增”中的“配置”中配置postDataTransformerBean属性，属性值是注册的bean id.
 *
 * 注意：1.必须实现{@link PostDataTransformer}接口，泛型对应transform后返回的结果类型。
 *       2.postDataTransformerBean配置值必须和注册的bean id保持一致。
 *       3.如果transform中出现异常，直接抛出就可以，外层会自动处理
 *       4.当返回值为null的时候，{@link DataSender}会忽略transform后的数据
 *       5.当返回值类型为Void的时候，{@link DataSender}不会再处理
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/10/18
 */
public class StringPostDataTransformer implements PostDataTransformer<String> {
    @Override
    public String transform(List<RowMetaData> rowMetaDataList) {
        String jsonString = JSONObject.toJSONString(rowMetaDataList);
        return jsonString;
    }
}
