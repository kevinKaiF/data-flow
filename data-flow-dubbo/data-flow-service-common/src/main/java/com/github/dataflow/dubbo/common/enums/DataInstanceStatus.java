package com.github.dataflow.dubbo.common.enums;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/5/31
 */
public enum DataInstanceStatus {
    /**
     * DataInstance正在从表单生成，尚不能启动和关停
     */
    CREATING(-1),
    /**
     * DataInstance从表单已生成，可以启动和关停
     */
    CREATED(0),
    /**
     * DataInstance已在Node中启动
     */
    START(1),
    /**
     * DataInstance已在Node中关停
     */
    STOP(2);

    private Integer status;

    DataInstanceStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
