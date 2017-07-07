package com.github.dataflow.transformer.core.post;

import com.github.dataflow.transformer.core.DataTransformer;

/**
 * 后置转换器，用于List<RowMetaData>到String的转换处理，默认使用{@link JSONDataTransformer}处理
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/7
 */
public interface PostDataTransformer extends DataTransformer<String> {
}
