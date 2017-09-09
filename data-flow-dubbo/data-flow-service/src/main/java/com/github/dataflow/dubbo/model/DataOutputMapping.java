package com.github.dataflow.dubbo.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author kevin
 * @date 2017-05-29 11:03 PM.
 */
public class DataOutputMapping implements Serializable {
    private static final long serialVersionUID = 4096835717936946773L;
    /**
     * @描述:主键
     * @字段:ID BIGINT(20)
     */
    private java.lang.Long id;

    /**
     * @描述:DataInstance的主键
     * @字段:DATA_INSTANCE_ID BIGINT(20)
     */
    @NotNull
    private java.lang.Long dataInstanceId;

    /**
     * @描述:库名
     * @字段:SCHEMA_NAME VARCHAR(50)
     */
    @NotNull
    private java.lang.String schemaName;

    /**
     * @描述:其他配置，JSON格式
     * @字段:OPTIONS VARCHAR(200)
     */
    private java.lang.String options;

    /**
     * * @描述:DataSourceOutput的主键
     *
     * @字段:DATA_INSTANCE_ID BIGINT(20)
     */
    @NotNull
    private java.lang.Long dataSourceOutputId;

    private DataSourceOutput dataSourceOutput;

    private String transformScript;

    /**
     *
     */
    public DataOutputMapping() {
    }

    /**
     * @param id 主键
     */
    public DataOutputMapping(java.lang.Long id) {
        this.id = id;
    }

    /**
     * @param id 主键
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }

    /**
     * @return 主键
     */
    public java.lang.Long getId() {
        return this.id;
    }

    /**
     * @param dataInstanceId DataInstance的主键
     */
    public void setDataInstanceId(java.lang.Long dataInstanceId) {
        this.dataInstanceId = dataInstanceId;
    }

    /**
     * @return DataInstance的主键
     */
    public java.lang.Long getDataInstanceId() {
        return this.dataInstanceId;
    }

    /**
     * @param schemaName 库名
     */
    public void setSchemaName(java.lang.String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * @return 库名
     */
    public java.lang.String getSchemaName() {
        return this.schemaName;
    }

    /**
     * @param options 其他配置，JSON格式
     */
    public void setOptions(java.lang.String options) {
        this.options = options;
    }

    /**
     * @return 其他配置，JSON格式
     */
    public java.lang.String getOptions() {
        return this.options;
    }

    public DataSourceOutput getDataSourceOutput() {
        return dataSourceOutput;
    }

    public void setDataSourceOutput(DataSourceOutput dataSourceOutput) {
        this.dataSourceOutput = dataSourceOutput;
    }

    public Long getDataSourceOutputId() {
        return dataSourceOutputId;
    }

    public void setDataSourceOutputId(Long dataSourceOutputId) {
        this.dataSourceOutputId = dataSourceOutputId;
    }

    @Override
    public String toString() {
        return "DataOutputMapping{" +
               "id=" + id +
               ", dataInstanceId=" + dataInstanceId +
               ", schemaName='" + schemaName + '\'' +
               ", options='" + options + '\'' +
               ", transformScript='" + transformScript + '\'' +
               '}';
    }

    public String getTransformScript() {
        return transformScript;
    }

    public void setTransformScript(String transformScript) {
        this.transformScript = transformScript;
    }
}
