package com.github.dataflow.transformer.core.pre;

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
public class TestPreGroovyShellDataTransformer {
    private List<RowMetaData> rowMetaDataList = new ArrayList<>();

    @Before
    public void before() {
        rowMetaDataList.add(new RowMetaData("testTable", "testSchema", RowMetaData.EventType.INSERT, new ArrayList<RowMetaData.ColumnMeta>(), new ArrayList<RowMetaData.ColumnMeta>()));
        rowMetaDataList.add(new RowMetaData("testTable", "testSchema", RowMetaData.EventType.UPDATE, new ArrayList<RowMetaData.ColumnMeta>(), new ArrayList<RowMetaData.ColumnMeta>()));
        rowMetaDataList.add(new RowMetaData("testTable", "testSchema", RowMetaData.EventType.DELETE, new ArrayList<RowMetaData.ColumnMeta>(), new ArrayList<RowMetaData.ColumnMeta>()));
    }

    @Test
    public void testTransformer() {
        String script = "public List<RowMetaData> transform(List<RowMetaData> rowMetaDataList) {\n"
                        + "        // 自定义的处理转换\n"
                        + "        for (RowMetaData rowMetaData : rowMetaDataList) {\n"
                        + "            if (\"testTable\".equals(rowMetaData.getTableName()) && \"testSchema\".equals(rowMetaData.getSchemaName())) {\n"
                        + "                // 更换表名\n"
                        + "                rowMetaData.setTableName(\"newTestTable\");\n"
                        + "                // 添加字段\n"
                        + "                if (rowMetaData.getEventType().equals(EventType.INSERT)) {\n"
                        + "                    rowMetaData.getAfterColumns().add(new ColumnMeta(\"test\", 4, false, \"10\"));\n"
                        + "                }\n"
                        + "            }\n"
                        + "\n"
                        + "            if (\"testSchema\".equals(rowMetaData.getSchemaName())) {\n"
                        + "                // 更换库名\n"
                        + "                rowMetaData.setSchemaName(\"newTestSchema\");\n"
                        + "            }\n"
                        + "        }\n"
                        + "        return rowMetaDataList;\n"
                        + "    }";

        PreGroovyShellDataTransformer preGroovyShellDataTransformer = new PreGroovyShellDataTransformer(script);
        List<RowMetaData> transformedList = preGroovyShellDataTransformer.transform(rowMetaDataList);
        System.out.println(transformedList);
    }
}
