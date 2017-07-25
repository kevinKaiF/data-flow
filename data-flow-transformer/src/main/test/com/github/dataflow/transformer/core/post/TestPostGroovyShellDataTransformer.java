package com.github.dataflow.transformer.core.post;

import com.github.dataflow.common.model.RowMetaData;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/25
 */
public class TestPostGroovyShellDataTransformer {
    private List<RowMetaData> rowMetaDataList = new ArrayList<>();

    @Before
    public void before() {
        rowMetaDataList.add(new RowMetaData("testTable", "testSchema", RowMetaData.EventType.INSERT, new ArrayList<RowMetaData.ColumnMeta>(), new ArrayList<RowMetaData.ColumnMeta>()));
        rowMetaDataList.add(new RowMetaData("testTable", "testSchema", RowMetaData.EventType.UPDATE, new ArrayList<RowMetaData.ColumnMeta>(), new ArrayList<RowMetaData.ColumnMeta>()));
        rowMetaDataList.add(new RowMetaData("testTable", "testSchema", RowMetaData.EventType.DELETE, new ArrayList<RowMetaData.ColumnMeta>(), new ArrayList<RowMetaData.ColumnMeta>()));
    }

    @Test
    public void testTransformer() {
        String script = "import com.alibaba.fastjson.JSON;\n"
                        + "\n"
                        + "    public String transform(List<RowMetaData> rowMetaDataList) {\n"
                        + "        // 自定义的处理转换\n"
                        + "        return JSON.toJSONString(rowMetaDataList);\n"
                        + "    }";

        PostGroovyShellDataTransformer postGroovyShellDataTransformer = new PostGroovyShellDataTransformer(script);
        String transform = postGroovyShellDataTransformer.transform(rowMetaDataList);
        System.out.println(transform);
    }
}
