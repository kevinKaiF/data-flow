package com.github.dataflow.dubbo.common.enums;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/5/30
 */
public enum DataSourceOutputType {
    // traditional DB start with 10
    MYSQL(10),
    ORACLE(11),
    POSTGRESQL(12),
    SQLSERVER(13),

    // mq start with 20
    KAFKA(20),
    METAQ(21),
    RABBITMQ(22);

    // nosql DB start with 30

    private int type;

    DataSourceOutputType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static DataSourceOutputType parse(int type) {
        DataSourceOutputType[] dataSourceOutputTypes = DataSourceOutputType.values();
        for (DataSourceOutputType dataSourceOutputType : dataSourceOutputTypes) {
            if (dataSourceOutputType.getType() == type) {
                return dataSourceOutputType;
            }
        }

        return null;
    }

    public static int parseName(String type) {
        return DataSourceOutputType.valueOf(type.toUpperCase()).getType();
    }
}
