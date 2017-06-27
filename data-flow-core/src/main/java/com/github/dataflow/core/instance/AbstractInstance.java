package com.github.dataflow.core.instance;

import com.github.dataflow.common.model.AbstractDataFlowLifeCycle;
import com.github.dataflow.core.alarm.AlarmService;
import com.github.dataflow.core.store.DataStore;

/**
 * @author kevin
 * @date 2017-06-01 1:12 AM.
 */
public abstract class AbstractInstance extends AbstractDataFlowLifeCycle implements Instance {
    /**
     * 实例的id
     */
    protected Long id;

    /**
     * 实例的名称
     */
    protected String name;

    /**
     * 白名单正则过滤库表
     */
    protected String whiteFilter;

    /**
     * 黑名单正则过滤库表
     */
    protected String blackFilter;

    /**
     * 数据库的jdbc url
     */
    protected String jdbcUrl;

    /**
     * 告警服务
     */
    private AlarmService alarmService;

    /**
     * 数据处理
     */
    private DataStore dataStore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhiteFilter() {
        return whiteFilter;
    }

    public void setWhiteFilter(String whiteFilter) {
        this.whiteFilter = whiteFilter;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getBlackFilter() {
        return blackFilter;
    }

    public void setBlackFilter(String blackFilter) {
        this.blackFilter = blackFilter;
    }

    @Override
    public void setAlarmService(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }
}
