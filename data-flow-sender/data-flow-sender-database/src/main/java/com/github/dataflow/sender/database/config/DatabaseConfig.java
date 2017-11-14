package com.github.dataflow.sender.database.config;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/24
 */
public interface DatabaseConfig {
    interface MappingConfig extends com.github.dataflow.sender.core.config.MappingConfig{
        String BATCH = "batch";
    }
    String HOST = "host";

    String PORT = "port";

    String JDBC_URL = "jdbcUrl";

    String USERNAME = "username";

    String PASSWORD = "password";


}
