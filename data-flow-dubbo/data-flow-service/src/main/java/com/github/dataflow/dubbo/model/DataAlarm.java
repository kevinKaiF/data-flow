package com.github.dataflow.dubbo.model;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/5
 */
public class DataAlarm {

    private java.lang.Integer type;

    private java.lang.String options;

    public DataAlarm() {
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "DataAlarm{" +
               "type=" + type +
               ", options='" + options + '\'' +
               '}';
    }
}
