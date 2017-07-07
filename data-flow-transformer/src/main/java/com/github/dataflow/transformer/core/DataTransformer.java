package com.github.dataflow.transformer.core;


import com.github.dataflow.common.model.RowMetaData;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/5/29
 */
public interface DataTransformer<T> {
    /**
     * 对数据按需要进行转换
     *
     * @param rowMetaDataList
     * @return
     */
    T transform(List<RowMetaData> rowMetaDataList);
}
