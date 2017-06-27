package com.github.dataflow.dubbo.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 数据同步实例model
 *
 * @author kevin
 * @date 2017-05-29 9:53 PM.
 */
public class DataInstance implements Serializable {
    private static final long serialVersionUID = 6297392082713086603L;

    /**
     * @描述:主键
     * @字段:ID BIGINT(20)
     */
    private java.lang.Long id;

    /**
     * @描述:实例名称
     * @字段:NAME VARCHAR(50)
     */
    @NotEmpty
    private java.lang.String name;

    /**
     * @描述:从库id
     * @字段:SLAVE_ID BIGINT(20)
     */
    @NotNull
    private java.lang.Long slaveId;

    /**
     * @描述:数据库host
     * @字段:HOST VARCHAR(20)
     */
    @NotNull
    private java.lang.String host;

    /**
     * @描述:数据库port
     * @字段:PORT INT(10)
     */
    @NotNull
    private java.lang.Integer port;

    /**
     * @描述:jdbc链接
     * @字段:JDBC_URL VARCHAR(100)
     */
    @NotNull
    private java.lang.String jdbcUrl;

    /**
     * @描述:数据源类型，1=MySQL,2=Oracle,3=POSTGRESQL,4=SQLSERVER
     * @字段:TYPE INT(10)
     */
    @NotNull
    private java.lang.Integer type;

    /**
     * @描述:用户名
     * @字段:USERNAME VARCHAR(50)
     */
    @NotNull
    private java.lang.String username;

    /**
     * @描述:密码
     * @字段:PASSWORD VARCHAR(50)
     */
    @NotNull
    private java.lang.String password;

    /**
     * @描述:白名单正则过滤表
     * @字段:WHITE_FILTER VARCHAR(100)
     */
    private java.lang.String whiteFilter;

    /**
     * @描述:黑名单正则过滤表
     * @字段:BLACK_FILTER VARCHAR(100)
     */
    private java.lang.String blackFilter;

    /**
     * @描述:转换脚本
     * @字段:TRANSFORM_SCRIPT VARCHAR(2000)
     */
    private java.lang.String transformScript;

    /**
     * @描述:其他配置，JSON格式
     * @字段:OPTIONS VARCHAR(200)
     */
    private java.lang.String options;

    /**
     * @描述:实例所在node的zk路径
     * @字段:NODE_PATH VARCHAR(100)
     */
    private java.lang.String nodePath;

    /**
     * @描述:实例的状态,-1=正在创建，0=创建完成，1=启动状态，2=关停状态
     * @字段:STATUS INT(10)
     */
    private java.lang.Integer status;

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
     * 输出数据源映射
     */
    private List<DataOutputMapping> dataOutputMappings;

    /**
     * 过滤库表字段
     */
    private List<DataTable> dataTables;

    /**
     *
     */
    public DataInstance() {
    }

    /**
     * @param id 主键
     */
    public DataInstance(java.lang.Long id) {
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
     * @param name 实例名称
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * @return 实例名称
     */
    public java.lang.String getName() {
        return this.name;
    }

    /**
     * @param slaveId 从库id
     */
    public void setSlaveId(java.lang.Long slaveId) {
        this.slaveId = slaveId;
    }

    /**
     * @return 从库id
     */
    public java.lang.Long getSlaveId() {
        return this.slaveId;
    }

    /**
     * @param host 数据库host
     */
    public void setHost(java.lang.String host) {
        this.host = host;
    }

    /**
     * @return 数据库host
     */
    public java.lang.String getHost() {
        return this.host;
    }

    /**
     * @param port 数据库port
     */
    public void setPort(java.lang.Integer port) {
        this.port = port;
    }

    /**
     * @return 数据库port
     */
    public java.lang.Integer getPort() {
        return this.port;
    }

    /**
     * @param jdbcUrl jdbc链接
     */
    public void setJdbcUrl(java.lang.String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    /**
     * @return jdbc链接
     */
    public java.lang.String getJdbcUrl() {
        return this.jdbcUrl;
    }

    /**
     * @param type 数据源类型，1=MySQL,2=Oracle,3=POSTGRESQL,4=SQLSERVER
     */
    public void setType(java.lang.Integer type) {
        this.type = type;
    }

    /**
     * @return 数据源类型，1=MySQL,2=Oracle,3=POSTGRESQL,4=SQLSERVER
     */
    public java.lang.Integer getType() {
        return this.type;
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

    /**
     * @param whiteFilter 白名单正则过滤表
     */
    public void setWhiteFilter(java.lang.String whiteFilter) {
        this.whiteFilter = whiteFilter;
    }

    /**
     * @return 白名单正则过滤表
     */
    public java.lang.String getWhiteFilter() {
        return this.whiteFilter;
    }

    /**
     * @param blackFilter 黑名单正则过滤表
     */
    public void setBlackFilter(java.lang.String blackFilter) {
        this.blackFilter = blackFilter;
    }

    /**
     * @return 黑名单正则过滤表
     */
    public java.lang.String getBlackFilter() {
        return this.blackFilter;
    }

    /**
     * @param transformScript 转换脚本
     */
    public void setTransformScript(java.lang.String transformScript) {
        this.transformScript = transformScript;
    }

    /**
     * @return 转换脚本
     */
    public java.lang.String getTransformScript() {
        return this.transformScript;
    }

    /**
     * @param options 其他配置，JSON格式
     */
    public void setOptions(java.lang.String options) {
        this.options = options;
    }

    /**
     * @return 其他配置，JSON格式
     */
    public java.lang.String getOptions() {
        return this.options;
    }

    /**
     * @param nodePath 实例所在node的zk路径
     */
    public void setNodePath(java.lang.String nodePath) {
        this.nodePath = nodePath;
    }

    /**
     * @return 实例所在node的zk路径
     */
    public java.lang.String getNodePath() {
        return this.nodePath;
    }

    /**
     * @param status 实例的状态,-1=正在创建，0=创建完成，1=启动状态，2=关停状态
     */
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    /**
     * @return 实例的状态,-1=正在创建，0=创建完成，1=启动状态，2=关停状态
     */
    public java.lang.Integer getStatus() {
        return this.status;
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

    public List<DataOutputMapping> getDataOutputMappings() {
        return dataOutputMappings;
    }

    public void setDataOutputMappings(List<DataOutputMapping> dataOutputMappings) {
        this.dataOutputMappings = dataOutputMappings;
    }

    public List<DataTable> getDataTables() {
        return dataTables;
    }

    public void setDataTables(List<DataTable> dataTables) {
        this.dataTables = dataTables;
    }

    @Override
    public String toString() {
        return "DataInstance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slaveId=" + slaveId +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", type=" + type +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", whiteFilter='" + whiteFilter + '\'' +
                ", blackFilter='" + blackFilter + '\'' +
                ", transformScript='" + transformScript + '\'' +
                ", options='" + options + '\'' +
                ", nodePath='" + nodePath + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
