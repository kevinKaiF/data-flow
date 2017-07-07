package com.github.dataflow.sender.core.datasource;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/23
 */
public class DataSourceHolder {
    private Object dataSource;

    private DataSourceHolder() {
    }

    public DataSourceHolder(Object dataSource) {
        this.dataSource = dataSource;
    }

    public Object getDataSource() {
        return dataSource;
    }

    public void setDataSource(Object dataSource) {
        this.dataSource = dataSource;
    }
}
