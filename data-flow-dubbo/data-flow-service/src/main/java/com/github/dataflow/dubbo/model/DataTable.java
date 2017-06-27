package com.github.dataflow.dubbo.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author kevin
 * @date 2017-06-01 12:20 AM.
 */
public class DataTable implements Serializable {
    private static final long serialVersionUID = 1369234292757427914L;
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
     * @描述:表名
     * @字段:TABLE_NAME VARCHAR(50)
     */
    @NotNull
    private java.lang.String tableName;

    /**
     * @描述:过滤的字段
     * @字段:COLUMNS VARCHAR(2000)
     */
    @NotNull
    private java.lang.String columns;

    /**
     *
     */
    public DataTable() {
    }

    /**
     * @param id 主键
     */
    public DataTable(java.lang.Long id) {
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
     * @param tableName 表名
     */
    public void setTableName(java.lang.String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return 表名
     */
    public java.lang.String getTableName() {
        return this.tableName;
    }

    /**
     * @param columns 过滤的字段
     */
    public void setColumns(java.lang.String columns) {
        this.columns = columns;
    }

    /**
     * @return 过滤的字段
     */
    public java.lang.String getColumns() {
        return this.columns;
    }

    @Override
    public String toString() {
        return "DataTable{" +
                "id=" + id +
                ", dataInstanceId=" + dataInstanceId +
                ", schemaName='" + schemaName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columns='" + columns + '\'' +
                '}';
    }
}
