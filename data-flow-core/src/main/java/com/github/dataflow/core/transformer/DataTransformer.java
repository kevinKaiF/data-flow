package com.github.dataflow.core.transformer;


import com.github.dataflow.common.model.RowMetaData;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/5/29
 */
public interface DataTransformer {
    /**
     * 对数据按需要进行转换
     *
     * @param rowMetaDataList
     * @return
     */
    List<RowMetaData> transform(List<RowMetaData> rowMetaDataList);
}
