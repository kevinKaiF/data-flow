package com.github.dataflow.transformer.core.pre;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.transformer.core.DataTransformer;

import java.util.List;

/**
 * 前置转换器，List<RowMetaData>到List<RowMetaData>的转换，用于schemaName,tableName,column等处理
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/7
 */
public interface PreDataTransformer extends DataTransformer<List<RowMetaData>> {
}
