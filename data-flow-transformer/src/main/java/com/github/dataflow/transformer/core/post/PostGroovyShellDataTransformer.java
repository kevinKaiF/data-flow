package com.github.dataflow.transformer.core.post;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.transformer.core.GroovyShellDataTransformer;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/7
 */
public class PostGroovyShellDataTransformer implements PostDataTransformer<Object> {
    private GroovyShellDataTransformer<Object> groovyShellDataTransformer;

    private PostGroovyShellDataTransformer() {
    }

    public PostGroovyShellDataTransformer(String transformScript) {
        this.groovyShellDataTransformer = new GroovyShellDataTransformer<>(transformScript);
    }

    @Override
    public Object transform(List<RowMetaData> rowMetaDataList) {
        return groovyShellDataTransformer.transform(rowMetaDataList);
    }

}
