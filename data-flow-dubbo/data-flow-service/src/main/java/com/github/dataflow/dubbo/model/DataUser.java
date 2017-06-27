package com.github.dataflow.dubbo.model;

import java.io.Serializable;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/7
 */
public class DataUser implements Serializable {
    private static final long serialVersionUID = -7669331601259109109L;
    /**
     * @描述:主键
     * @字段:ID BIGINT(20)
     */
    private java.lang.Long id;

    /**
     * @描述:用户名
     * @字段:USERNAME VARCHAR(20)
     */
    private java.lang.String username;

    /**
     * @描述:密码
     * @字段:PASSWORD VARCHAR(100)
     */
    private java.lang.String password;

    /**
     *
     */
    public DataUser() {
    }

    /**
     * @param id 主键
     */
    public DataUser(java.lang.Long id) {
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
     * @param username 用户名
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    /**
     * @return 用户名
     */
    public java.lang.String getUsername() {
        return this.username;
    }

    /**
     * @param password 密码
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    /**
     * @return 密码
     */
    public java.lang.String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return "DataUser{" +
                "username='" + username + '\'' +
                ", id=" + id +
                '}';
    }
}
