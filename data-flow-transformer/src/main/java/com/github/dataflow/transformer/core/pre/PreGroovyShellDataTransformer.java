package com.github.dataflow.transformer.core.pre;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.transformer.core.GroovyShellDataTransformer;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/7
 */
public class PreGroovyShellDataTransformer implements PreDataTransformer {
    private GroovyShellDataTransformer<List<RowMetaData>> groovyShellDataTransformer;

    private PreGroovyShellDataTransformer() {
    }

    public PreGroovyShellDataTransformer(String transformScript) {
        this.groovyShellDataTransformer = new GroovyShellDataTransformer<>(transformScript);
    }

    @Override
    public List<RowMetaData> transform(List<RowMetaData> rowMetaDataList) {
        return groovyShellDataTransformer.transform(rowMetaDataList);
    }

}
