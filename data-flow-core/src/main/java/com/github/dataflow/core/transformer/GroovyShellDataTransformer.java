package com.github.dataflow.core.transformer;

import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.core.exception.DataTransformerException;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin
 * @date 2017-05-29 10:11 PM.
 */
public class GroovyShellDataTransformer implements DataTransformer {
    private static final String IMPORT_ROW_META_DATA = "import com.github.dataflow.common.model.RowMetaData;\n\r";

    private static final String SCRIPT_METHOD_NAME = "transform";

    private String transformScript;

    // 缓存Script，防止Groovy脚本执行时，ClassLoader多次加载造成Perm区溢出
    private Script scriptInvoker;

    public GroovyShellDataTransformer() {
    }

    public GroovyShellDataTransformer(String transformScript) {
        validateTransformScript(transformScript);
        this.transformScript = transformScript;
    }

    private void validateTransformScript(String transformScript) {
        try {
            String scriptText = buildScriptText(transformScript);
            Script script = new GroovyShell().parse(scriptText);
            script.invokeMethod(SCRIPT_METHOD_NAME, new ArrayList<RowMetaData>());
            this.scriptInvoker = script;
        } catch (Exception e) {
            throw new DataTransformerException(e);
        }
    }

    private String buildScriptText(String transformScript) {
        return IMPORT_ROW_META_DATA + transformScript;
    }

    public List<RowMetaData> transform(List<RowMetaData> rowMetaDataList) {
        try {
            return (List<RowMetaData>) scriptInvoker.invokeMethod(SCRIPT_METHOD_NAME, rowMetaDataList);
        } catch (Exception e) {
            throw new DataTransformerException();
        }
    }

    public String getTransformScript() {
        return transformScript;
    }

}
