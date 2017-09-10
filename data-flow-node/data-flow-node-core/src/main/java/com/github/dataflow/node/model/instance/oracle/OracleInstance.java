package com.github.dataflow.node.model.instance.oracle;

import com.github.dataflow.core.instance.AbstractInstance;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/9/10
 */
public class OracleInstance extends AbstractInstance {
    // 需要同步的表名
    private Set<String>     tableNames         = new HashSet<>();
    // 每次拉取的数量
    private int             pageSize           = 200;
    // 每张表同步的次数
    private int             syncNumber         = 10;
    // 同步的线程数目
    private int             syncThreadNumber   = 1;
    // 同步线程池
    private ExecutorService executorService    = null;
    // 始终包含主键
    private boolean         containsPrimaryKey = true;

    @Override
    protected void doInit() {
        // 初始化数据源
        // 初始化需要同步的表
        // 创建物化日志
    }

    @Override
    protected void doStart() {
        // 开启线程开始同步，依次同步tableNames，需要支持全量同步?TODO
    }

    @Override
    protected void doStop() {
        // 关闭线程池
    }

    @Override
    public String getPosition(String instanceName) {
        return null;
    }
}
