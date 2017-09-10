package com.github.dataflow.sender.local;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.sender.core.DataSender;

import java.io.File;
import java.util.List;

/**
 * 自定义自己的逻辑
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/9/9
 */
public class LocalSender extends DataSender {
    private JSONObject props;

    @Override
    public boolean isSingleton() {
        return false;
    }


    /**
     * 可以定义自己逻辑，可以在输出源定义配置等
     */
    @Override
    public void send(List<RowMetaData> rowMetaDataList) throws Exception {
        // demo1:简单的打印
        for (RowMetaData rowMetaData : rowMetaDataList) {
            System.out.println(rowMetaData);
        }

        // demo2:写入文件
        String filePath = JSONObjectUtil.getString(props, "filePath");
        File file = new File(filePath);
        if (file.exists()) {
            // 写入文件等等
        }

    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {

    }

    public JSONObject getProps() {
        return props;
    }

    public void setProps(JSONObject props) {
        this.props = props;
    }
}
