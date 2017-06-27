package com.github.dataflow.dubbo.common.enums;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/5
 */
public enum DataAlarmType {
    /**
     * 邮件
     */
    MAIL(1),
    /**
     * 微信
     */
    WECHAT(2),
    /**
     * 钉钉
     */
    DINGTALK(3);

    private int type;

    DataAlarmType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
