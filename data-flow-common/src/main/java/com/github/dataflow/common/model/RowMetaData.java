package com.github.dataflow.common.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author kevin
 * @date 2017-05-29 9:58 PM.
 */
public class RowMetaData implements Serializable {

    private static final long serialVersionUID = -1984762920188652206L;

    private String tableName;

    private String schemaName;

    private EventType eventType;

    private List<ColumnMeta> beforeColumns;

    private List<ColumnMeta> afterColumns;

    public RowMetaData() {
    }

    public RowMetaData(String tableName, String schemaName, EventType eventType, List<ColumnMeta> beforeColumns, List<ColumnMeta> afterColumns) {
        this.tableName = tableName;
        this.schemaName = schemaName;
        this.eventType = eventType;
        this.beforeColumns = beforeColumns;
        this.afterColumns = afterColumns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public List<ColumnMeta> getBeforeColumns() {
        return beforeColumns;
    }

    public void setBeforeColumns(List<ColumnMeta> beforeColumns) {
        this.beforeColumns = beforeColumns;
    }

    public List<ColumnMeta> getAfterColumns() {
        return afterColumns;
    }

    public void setAfterColumns(List<ColumnMeta> afterColumns) {
        this.afterColumns = afterColumns;
    }

    public String getFullTableName() {
        return schemaName + "." + tableName;
    }

    @Override
    public String toString() {
        return "RowMetaData{" +
                "tableName='" + tableName + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", eventType=" + eventType +
                ", beforeColumns=" + beforeColumns +
                ", afterColumns=" + afterColumns +
                '}';
    }

    public static class ColumnMeta {
        private String columnName;

        private int jdbcType;

        private boolean isKey;

        private String value;

        public ColumnMeta() {
        }

        public ColumnMeta(String columnName, int jdbcType, boolean isKey, String value) {
            this.columnName = columnName;
            this.jdbcType = jdbcType;
            this.isKey = isKey;
            this.value = value;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public int getJdbcType() {
            return jdbcType;
        }

        public void setJdbcType(int jdbcType) {
            this.jdbcType = jdbcType;
        }

        public boolean isKey() {
            return isKey;
        }

        public void setKey(boolean key) {
            isKey = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ColumnMeta{" +
                    "columnName='" + columnName + '\'' +
                    ", jdbcType=" + jdbcType +
                    ", isKey=" + isKey +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public enum EventType {
        INSERT,
        UPDATE,
        DELETE;

        EventType() {
        }
    }
}
