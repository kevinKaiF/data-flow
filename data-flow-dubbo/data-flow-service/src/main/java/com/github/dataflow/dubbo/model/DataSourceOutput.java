package com.github.dataflow.dubbo.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author kevin
 * @date 2017-05-29 11:05 PM.
 */
public class DataSourceOutput implements Serializable {
    private static final long serialVersionUID = 2090433809038786104L;
    /**
     * @描述:主键
     * @字段:ID BIGINT(20)
     */
    private java.lang.Long id;

    /**
     * @描述:输出数据源的类型
     * @字段:TYPE INT(10)
     */
    @NotNull
    private java.lang.Integer type;

    /**
     * @描述:输出数据源的名称
     * @字段:NAME VARCHAR(20)
     */
    @NotNull
    private java.lang.String name;

    /**
     * @描述:输出数据源的配置，JSON格式
     * @字段:OPTIONS VARCHAR(200)
     */
    @NotNull
    private java.lang.String options;

    /**
     * @描述:创建时间
     * @字段:CREATE_TIME DATETIME
     */
    private java.util.Date createTime;

    /**
     * 非数据库字段，查询时使用
     */
    private java.util.Date createTimeBegin;

    /**
     * 非数据库字段，查询时使用
     */
    private java.util.Date createTimeEnd;

    /**
     * @描述:更新时间
     * @字段:UPDATE_TIME DATETIME
     */
    private java.util.Date updateTime;

    /**
     * 非数据库字段，查询时使用
     */
    private java.util.Date updateTimeBegin;

    /**
     * 非数据库字段，查询时使用
     */
    private java.util.Date updateTimeEnd;

    /**
     *
     */
    public DataSourceOutput() {
    }

    /**
     * @param id 主键
     */
    public DataSourceOutput(java.lang.Long id) {
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
     * @param type 输出数据源的类型
     */
    public void setType(java.lang.Integer type) {
        this.type = type;
    }

    /**
     * @return 输出数据源的类型
     */
    public java.lang.Integer getType() {
        return this.type;
    }

    /**
     * @param options 输出数据源的配置，JSON格式
     */
    public void setOptions(java.lang.String options) {
        this.options = options;
    }

    /**
     * @return 输出数据源的配置，JSON格式
     */
    public java.lang.String getOptions() {
        return this.options;
    }

    /**
     * @param createTime 创建时间
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 创建时间
     */
    public java.util.Date getCreateTime() {
        return this.createTime;
    }

    /**
     * @param createTimeBegin 创建时间开始
     */
    public void setCreateTimeBegin(java.util.Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    /**
     * @return 创建时间开始
     */
    public java.util.Date getCreateTimeBegin() {
        return this.createTimeBegin;
    }

    /**
     * @param createTimeEnd 创建时间结束
     */
    public void setCreateTimeEnd(java.util.Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    /**
     * @return 创建时间结束
     */
    public java.util.Date getCreateTimeEnd() {
        return this.createTimeEnd;
    }

    /**
     * @param updateTime 更新时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return 更新时间
     */
    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * @param updateTimeBegin 更新时间开始
     */
    public void setUpdateTimeBegin(java.util.Date updateTimeBegin) {
        this.updateTimeBegin = updateTimeBegin;
    }

    /**
     * @return 更新时间开始
     */
    public java.util.Date getUpdateTimeBegin() {
        return this.updateTimeBegin;
    }

    /**
     * @param updateTimeEnd 更新时间结束
     */
    public void setUpdateTimeEnd(java.util.Date updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
    }

    /**
     * @return 更新时间结束
     */
    public java.util.Date getUpdateTimeEnd() {
        return this.updateTimeEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DataSourceOutput{" +
               "id=" + id +
               ", type=" + type +
               ", name=" + name +
               ", options='" + options + '\'' +
               ", createTime=" + createTime +
               ", updateTime=" + updateTime +
               '}';
    }
}
