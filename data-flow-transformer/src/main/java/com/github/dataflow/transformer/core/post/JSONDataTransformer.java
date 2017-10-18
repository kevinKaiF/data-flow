package com.github.dataflow.transformer.core.post;

import com.alibaba.fastjson.JSON;
import com.github.dataflow.common.model.RowMetaData;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/6
 */
public class JSONDataTransformer implements PostDataTransformer<String> {
    @Override
    public String transform(List<RowMetaData> rowMetaDataList) {
        return JSON.toJSONString(rowMetaDataList);
    }
}
