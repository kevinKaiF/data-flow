/*
 * Copyright (c) 2001-2014 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有 
 */
package com.github.dataflow.dubbo.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <code>DataNodeConfig</code>数据库映射.
 *
 * @version : Ver 1.0
 * @table : data_node_config
 * @author    : kevin
 * @date    : 2017-06-14 下午14:13:38
 */
public class DataNodeConfiguration implements Serializable {

    private static final long serialVersionUID = -6497606965681646956L;
    /**
     * @描述:主键
     * @字段:ID BIGINT(19)
     */
    private Long id;

    /**
     * @描述:告警的类型
     * @字段:TYPE INT(10)
     */
    @NotNull
    private Integer type;

    /**
     * @描述:告警的配置，JSON格式
     * @字段:OPTIONS VARCHAR(200)
     */
    @NotNull
    private String options;

    /**
     *
     */
    public DataNodeConfiguration() {
    }

    /**
     * @param id 主键
     */
    public DataNodeConfiguration(Long id) {
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
     * @param type 告警的类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return 告警的类型
     */
    public Integer getType() {
        return this.type;
    }

    /**
     * @param options 告警的配置，JSON格式
     */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * @return 告警的配置，JSON格式
     */
    public String getOptions() {
        return this.options;
    }

    @Override
    public String toString() {
        return "DataNodeConfiguration{" +
                "id=" + id +
                ", type=" + type +
                ", options='" + options + '\'' +
                '}';
    }
}
