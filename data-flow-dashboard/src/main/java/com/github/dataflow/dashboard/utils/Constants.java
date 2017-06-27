package com.github.dataflow.dashboard.utils;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/8
 */
public class Constants {
    /**
     * list查询，分页条数属性名
     */
    public static final String RECORDS_TOTAL = "recordsTotal";

    /**
     * list查询，总数属性名
     */
    public static final String RECORDS_FILTERED = "recordsFiltered";

    /**
     * list查询，结果数据属性名
     */
    public static final String DATA = "data";

    /**
     * session属性
     */
    public static final String SESSION_USER = "user";

    /**
     * 请求node start/stop 的key
     */
    public static String DATA_INSTANCE_KEY = "dataInstance";

    /**
     * node中启动dataInstance的uri
     */
    public static final String START_WITH_JSON_URI = "/node/instance/startWithJson";

    /**
     * node中关停dataInstance的uri
     */
    public static final String STOP_WITH_JSON_URI = "/node/instance/stopWithJson";

    /**
     * 请求node instanceDetail的key
     */
    public static final String INSTANCE_NAME_KEY = "instanceName";

    /**
     * node中instance的详细信息
     */
    public static final String NODE_INSTANCE_DETAIL = "/node/node/instanceDetail";
}
