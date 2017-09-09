/*
 * Copyright (c) 2001-2014 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有 
 */
package com.github.dataflow.dubbo.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <code>DataLog</code>数据库映射.
 *
 * @author : kevin
 * @version : Ver 1.0
 * @table : data_log
 * @date : 2017-06-26 上午09:32:27
 */
public class DataLog implements Serializable {

    private static final long serialVersionUID = -5290145389121608639L;
    /**
     * @描述:主键
     * @字段:ID BIGINT(20)
     */
    private Long id;

    /**
     * @描述:实例名称
     * @字段:INSTANCE_NAME VARCHAR(50)
     */
    @NotNull
    private String instanceName;

    /**
     * @描述:日志信息
     * @字段:MESSAGE VARCHAR(3000)
     */
    @NotNull
    private String message;

    /**
     * @描述:创建时间
     * @字段:CREATE_TIME DATETIME(20)
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
     *
     */
    public DataLog() {
    }

    /**
     * @param id 主键
     */
    public DataLog(Long id) {
        this.id = id;
    }

    /**
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return 主键
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param instanceName 实例名称
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * @return 实例名称
     */
    public String getInstanceName() {
        return this.instanceName;
    }

    /**
     * @param message 日志信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return 日志信息
     */
    public String getMessage() {
        return this.message;
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

    @Override
    public String toString() {
        return "DataLog{" +
               "id=" + id +
               ", instanceName='" + instanceName + '\'' +
               ", message='" + message + '\'' +
               ", createTime=" + createTime +
               '}';
    }
}
