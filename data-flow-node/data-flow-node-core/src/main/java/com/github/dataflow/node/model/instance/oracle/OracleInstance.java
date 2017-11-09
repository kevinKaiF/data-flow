package com.github.dataflow.node.model.instance.oracle;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.utils.Closer;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.core.instance.AbstractInstance;
import com.github.dataflow.node.model.instance.oracle.config.OracleInstanceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/11/8
 */
public class OracleInstance extends AbstractInstance {
    private Logger logger = LoggerFactory.getLogger(OracleInstance.class);

    private Semaphore semaphore = new Semaphore(0);

    private AtomicLong atomicLong = new AtomicLong(0);

    private int queryCount;

    private int queryPageSize;

    // 需要同步的表
    private List<String> syncTables;

    private JSONObject options;

    private DataSource oracleDataSource;

    private MaterializedViewLogHandler                        materializedViewLogHandler;
    // 缓存TableMeta
    private Map<String, MaterializedViewLogHandler.TableMeta> tableMetaMap;
    // 同步表的下标
    private AtomicInteger index = new AtomicInteger(0);

    private Thread producer;

    private OracleInstance() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public OracleInstance(JSONObject options, DataSource oracleDataSource) {
        this.options = options;
        this.oracleDataSource = oracleDataSource;
        this.queryCount = JSONObjectUtil.getInt(options, OracleInstanceConfig.MAX_QUERY_COUNT, OracleInstanceConfig.DEFAULT_MAX_QUERY_COUNT);
        this.queryPageSize = JSONObjectUtil.getInt(options, OracleInstanceConfig.QUERY_PAGE_SIZE, OracleInstanceConfig.DEFAULT_QUERY_PAGE_SIZE);
        this.materializedViewLogHandler = new MaterializedViewLogHandler(oracleDataSource, queryPageSize);
        this.tableMetaMap = new WeakHashMap<>();
    }

    @Override
    protected void doInit() {
        logger.info("init OracleInstance for {}-{} with parameters:{}", new Object[]{this.id, this.name, this.options});
        initSyncTables();
        initProducer();

    }

    private void initProducer() {
        // 初始化生产者
        this.producer = new Thread(new OracleInstanceTask());
        this.producer.setName("OracleInstanceThread-" + name + "-" + atomicLong.getAndIncrement());
        this.producer.setDaemon(true);
    }

    /**
     * 获取所有符合条件的表名，eg.schema + table格式
     */
    private void initSyncTables() {
        String whiteFilter = JSONObjectUtil.getString(options, OracleInstanceConfig.WHITE_FILTER);
        String blackFilter = JSONObjectUtil.getString(options, OracleInstanceConfig.BLACK_FILTER);
        this.syncTables = SyncTableNameParser.parse(oracleDataSource, whiteFilter, blackFilter);
    }

    private List<String> getTableNames(Connection connection, String schemaName, Map<String, String> blackTableMap) throws SQLException {
        ResultSet resultSet = null;
        List<String> tableNameList = new ArrayList<>();
        try {
            resultSet = connection.getMetaData().getTables(schemaName, null, "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                tableNameList.add(schemaName + "." + resultSet.getString("TABLE_NAME"));
            }
        } finally {
            Closer.closeQuietly(resultSet);
        }
        return tableNameList;
    }

    @Override
    protected void doStart() {
        logger.info("init OracleInstanceProducer producer begin...");
        this.producer.start();
        logger.info("init OracleInstanceProducer producer end!");
    }

    @Override
    protected void doStop() {
        try {
            semaphore.acquire(1);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        if (dataStore.isStart()) {
            dataStore.stop();
        }
    }

    @Override
    public String getPosition(String instanceName) {
        return null;
    }

    public class OracleInstanceTask implements Runnable {
        // 同步次数统计
        private int count = 0;
        @Override
        public void run() {
            Throwable ex = null;
            while (running && ex == null) {
                try {
                    String fullTableName = nextFullTableName();
                    MaterializedViewLogHandler.TableMeta tableMeta = queryTableMeta(fullTableName);
                    // 从物化日志获取数据
                    QueryViewLogCallback.RowMetaDataHolder rowMetaDataHolder = materializedViewLogHandler.queryViewLog(tableMeta);
                    if (CollectionUtils.isEmpty(rowMetaDataHolder.getRowMetaDataList())) {
                        index.getAndIncrement();
                        count = 0;
                    } else {
                        // 发送数据
                        dataStore.handle(rowMetaDataHolder.getRowMetaDataList());
                        // 删除物化日志
                        materializedViewLogHandler.deleteViewLog(tableMeta, rowMetaDataHolder.getRowIds());
                        count++;
                    }
                } catch (Exception e) {

                } finally {
                    if (ex != null) {
                        stop();
                    }
                }
            }
        }

        private MaterializedViewLogHandler.TableMeta queryTableMeta(String fullTableName) throws SQLException {
            MaterializedViewLogHandler.TableMeta tableMeta = tableMetaMap.get(fullTableName);
            if (tableMeta == null) {
                // 获取表的元数据
                tableMeta = materializedViewLogHandler.queryTableMeta(fullTableName);
                tableMetaMap.put(fullTableName, tableMeta);
            }
            return tableMeta;
        }

        private String nextFullTableName() {
            String fullTableName = null;
            // 如果超过查询次数，则换下一张表
            if (count < queryCount) {
                fullTableName = getFullTableName();
            } else {
                index.getAndIncrement();
                count = 0;
                fullTableName = getFullTableName();
            }
            return fullTableName;
        }

        private String getFullTableName() {
            int size = syncTables.size();
            if (index.get() >= size) {
                index.compareAndSet(size, 0);
            }
            return syncTables.get(index.get());
        }

        private void stop() {
            semaphore.release();
            doStop();
            logger.info("stop OracleInstanceTask[instanceName:{}] successfully.", name);
        }
    }

}
