package com.github.dataflow.node.model.instance.oracle.config;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/9/10
 */
public interface OracleInstanceConfig {
    String HOST = "host";

    String PORT = "port";

    String JDBC_URL = "jdbcUrl";

    String USERNAME = "username";

    String PASSWORD = "password";

    // 白名单
    String WHITE_FILTER = "whiteFilter";

    // 黑名单
    String BLACK_FILTER = "blackFilter";

    // 每张表最大查询次数
    String MAX_QUERY_COUNT = "maxQueryCount";
    int DEFAULT_MAX_QUERY_COUNT = 10;

    // 分页的条数
    String QUERY_PAGE_SIZE = "queryPageSize";
    int DEFAULT_QUERY_PAGE_SIZE = 200;
}
