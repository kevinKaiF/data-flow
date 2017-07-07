package com.github.dataflow.transformer.core;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.transformer.exception.DataTransformerException;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin
 * @date 2017-05-29 10:11 PM.
 */
public class GroovyShellDataTransformer<T> implements DataTransformer<T> {
    private static final String IMPORT_ROW_META_DATA = "import com.github.dataflow.common.model.RowMetaData;\n\r";

    private static final String SCRIPT_METHOD_NAME = "transform";

    // 缓存Script，防止Groovy脚本执行时，ClassLoader多次加载造成Perm区溢出
    private Script scriptInvoker;

    private GroovyShellDataTransformer() {
    }

    public GroovyShellDataTransformer(String transformScript) {
        validateTransformScript(transformScript);
    }

    private void validateTransformScript(String transformScript) {
        try {
            String scriptText = buildScriptText(transformScript);
            Script script = new GroovyShell().parse(scriptText);
            script.invokeMethod(SCRIPT_METHOD_NAME, newTestRowMetaData());
            this.scriptInvoker = script;
        } catch (Exception e) {
            throw new DataTransformerException(e);
        }
    }

    /**
     * 创建测试数据
     *
     * @return
     */
    private ArrayList<RowMetaData> newTestRowMetaData() {
        ArrayList<RowMetaData> rowMetaDataArrayList = new ArrayList<>();
        // build rowMetaData
        RowMetaData rowMetaData = new RowMetaData();
        rowMetaData.setSchemaName("test");
        rowMetaData.setTableName("test");
        rowMetaData.setEventType(RowMetaData.EventType.INSERT);
        // build columnMeta
        List<RowMetaData.ColumnMeta> columnMetaList = new ArrayList<>();
        columnMetaList.add(new RowMetaData.ColumnMeta("ID", Types.TINYINT, Boolean.TRUE, "1"));
        rowMetaData.setAfterColumns(columnMetaList);
        // add rowMetaData
        rowMetaDataArrayList.add(rowMetaData);
        return rowMetaDataArrayList;
    }

    private String buildScriptText(String transformScript) {
        return IMPORT_ROW_META_DATA + transformScript;
    }

    public T transform(List<RowMetaData> rowMetaDataList) {
        try {
            return (T) scriptInvoker.invokeMethod(SCRIPT_METHOD_NAME, rowMetaDataList);
        } catch (Exception e) {
            throw new DataTransformerException();
        }
    }

}
