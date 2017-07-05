package com.github.dataflow.dubbo.common.enums;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/5/30
 */
public enum DataSourceType {
    // traditional DB start with 10
    MYSQL(10),
    ORACLE(11),
    POSTGRESQL(12),
    SQLSERVER(13),
    HIVE(14),

    // mq start with 20
    KAFKA(20),
    METAQ(21),
    RABBITMQ(22),
    ACTIVEMQ(23);

    // nosql DB start with 30

    private int type;

    DataSourceType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static DataSourceType parse(int type) {
        DataSourceType[] dataSourceTypes = DataSourceType.values();
        for (DataSourceType dataSourceType : dataSourceTypes) {
            if (dataSourceType.getType() == type) {
                return dataSourceType;
            }
        }

        return null;
    }

    public static int parseName(String type) {
        return DataSourceType.valueOf(type.toUpperCase()).getType();
    }
}
