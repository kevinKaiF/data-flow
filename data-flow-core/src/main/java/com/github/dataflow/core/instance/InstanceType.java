package com.github.dataflow.core.instance;

/**
 * @author kevin
 * @date 2017-05-28 3:08 PM.
 */
public enum InstanceType {
    MYSQL(1),
    ORACLE(2),
    POSTGRESQL(3),
    SQLSERVER(4);

    private int type;

    InstanceType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

