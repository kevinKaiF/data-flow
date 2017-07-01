package com.github.dataflow.dubbo.model;

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
     * @字段:NAME VARCHAR(100)
     */
    private java.lang.String name;

    /**
     * @描述:数据源类型
     * @字段:TYPE INT(10)
     */
    @NotNull
    private java.lang.Integer type;

    /**
     * @描述:数据的生产者/消费者
     * @字段:PRODUCER_OR_CONSUMER INT(1)
     */
    @NotNull
    private java.lang.Integer producerOrConsumer;

    /**
     * @描述:针对于name的标签
     * @字段:TAG VARCHAR(20)
     */
    @NotNull
    private java.lang.String tag;

    /**
     * @描述:转换脚本
     * @字段:TRANSFORM_SCRIPT VARCHAR(2000)
     */
    private java.lang.String transformScript;

    /**
     * @描述:其他配置，JSON格式
     * @字段:OPTIONS VARCHAR(200)
     */
    @NotNull
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

    public Integer getProducerOrConsumer() {
        return producerOrConsumer;
    }

    public void setProducerOrConsumer(Integer producerOrConsumer) {
        this.producerOrConsumer = producerOrConsumer;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "DataInstance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", producerOrConsumer=" + producerOrConsumer +
                ", tag='" + tag + '\'' +
                ", transformScript='" + transformScript + '\'' +
                ", options='" + options + '\'' +
                ", nodePath='" + nodePath + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
