package com.github.dataflow.node.model.instance.oracle;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.common.utils.JSONObjectUtil;
import com.github.dataflow.node.model.instance.AbstractPooledInstance;
import com.github.dataflow.node.model.instance.PooledInstanceConfig;
import com.github.dataflow.node.model.instance.oracle.config.OracleInstanceConfig;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
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
public class OracleInstance extends AbstractPooledInstance {
    private Logger logger = LoggerFactory.getLogger(OracleInstance.class);

    private Semaphore semaphore = new Semaphore(0);

    private AtomicLong atomicLong = new AtomicLong(0);

    private int queryCount;

    private int queryPageSize;

    private int pollPeriod;

    // 需要同步的表
    private List<String> syncTables;

    private DataSource oracleDataSource;

    private MaterializedViewLogHandler materializedViewLogHandler;
    // 缓存TableMeta
    private Map<String, MaterializedViewLogHandler.TableMeta> tableMetaMap;
    // 同步表的下标
    private AtomicInteger index = new AtomicInteger(0);

    private OracleInstance() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public OracleInstance(JSONObject options, DataSource oracleDataSource) {
        this.options = options;
        this.oracleDataSource = oracleDataSource;
        this.queryCount = JSONObjectUtil.getInt(options, OracleInstanceConfig.MAX_QUERY_COUNT, OracleInstanceConfig.DEFAULT_MAX_QUERY_COUNT);
        this.queryPageSize = JSONObjectUtil.getInt(options, OracleInstanceConfig.QUERY_PAGE_SIZE, OracleInstanceConfig.DEFAULT_QUERY_PAGE_SIZE);
        this.pollPeriod = JSONObjectUtil.getInt(options, PooledInstanceConfig.POLL_PERIOD);
        this.materializedViewLogHandler = new MaterializedViewLogHandler(oracleDataSource, queryPageSize);
        this.tableMetaMap = new WeakHashMap<>();
    }

    /**
     * 获取所有符合条件的表名，eg.schema + table格式
     */
    private void initSyncTables() {
        logger.info("init syncTables begin...");
        String whiteFilter = JSONObjectUtil.getString(options, OracleInstanceConfig.WHITE_FILTER);
        String blackFilter = JSONObjectUtil.getString(options, OracleInstanceConfig.BLACK_FILTER);
        this.syncTables = TableNameParser.parse(oracleDataSource, whiteFilter, blackFilter);
        logger.info("init syncTables end. \n\twhiteFilter:{}\n\tblackFilter:{}", whiteFilter, blackFilter);
    }

    @Override
    protected void doStart() {
        logger.info("start OracleInstance for {} / {} with parameters:{}", new Object[]{this.id, this.name, this.options});
        super.doStart();
        logger.info("start OracleInstance successfully.");
    }

    @Override
    protected void doStop() {
        logger.info("stop OracleInstance for {} / {} ", new Object[]{this.id, this.name});
        try {
            semaphore.acquire(1);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        if (dataStore.isStart()) {
            dataStore.stop();
        }
        logger.info("stop OracleInstance successfully.");
    }

    @Override
    protected void initConsumer() {
        logger.info("init consumer begin...");
        initSyncTables();
        logger.info("init consumer end");
    }

    @Override
    protected void initReceiveThread() {
        logger.info("init receive thread begin...");
        super.initReceiveThread();
        logger.info("init receive thread end!");
    }

    @Override
    protected String getThreadName() {
        return "oracleInstance-" + name + "-" + atomicLong.getAndIncrement();
    }

    @Override
    public String getPosition() {
        return null;
    }

    @Override
    protected ReceiveTask newReceiveTask() {
        return new ReceiveTask() {
            // 同步次数统计
            private int count = 0;

            @Override
            protected void closeConsumer() {
                semaphore.release();
                logger.info("stop OracleInstanceTask[instanceName:{}] successfully.", name);
            }

            @Override
            protected void handle(List<RowMetaData> rowMetaDataList) throws Exception {
                dataStore.handle(rowMetaDataList);
            }

            @Override
            public void run() {
                Throwable ex = null;
                while (running) {
                    try {
                        String fullTableName = nextFullTableName();
                        MaterializedViewLogHandler.TableMeta tableMeta = queryTableMeta(fullTableName);
                        // 从物化日志获取数据
                        QueryViewLogCallback.RowMetaDataHolder rowMetaDataHolder = materializedViewLogHandler.queryViewLog(tableMeta);
                        if (CollectionUtils.isEmpty(rowMetaDataHolder.getRowMetaDataList())) {
                            index.getAndIncrement();
                            count = 0;
                        } else {
                            // 数据是否已发送，处理物化日志删除失败情况
                            boolean handled = false;
                            do {
                                try {
                                    if (!handled) {
                                        // 发送数据
                                        List<RowMetaData> rowMetaDataList = rowMetaDataHolder.getRowMetaDataList();
                                        logger.debug("ReceiveTask receive data : " + rowMetaDataList);
                                        handle(rowMetaDataList);
                                        handled = true;
                                    }
                                    // 删除物化日志
                                    materializedViewLogHandler.deleteViewLog(tableMeta, rowMetaDataHolder.getRowIds());
                                    ex = null;
                                } catch (Exception e) {
                                    handleException(e);
                                    ex = e;
                                } finally {
                                    Thread.sleep(pollPeriod);
                                }
                            } while (running && ex != null && !(ex instanceof InterruptedException));
                            count++;
                        }
                        Thread.sleep(pollPeriod);
                    } catch (Throwable e) {
                        handleException(e);
                        ex = e;
                    } finally {
                        if (!running || ex instanceof InterruptedException) {
                            closeConsumer();
                            doStop();
                        }
                    }
                }
            }

            private void handleException(Throwable e) {
                if (e instanceof InterruptedException) {
                    logger.info("OracleInstanceTask accept interruption successfully.");
                } else {
                    logger.error("OracleInstanceTask happened exception, detail : ", e);
                    String fullStackTrace = ExceptionUtils.getFullStackTrace(e);
                    alarmService.sendAlarm(name, fullStackTrace);
                }
            }

            private MaterializedViewLogHandler.TableMeta queryTableMeta(String fullTableName) throws SQLException {
                MaterializedViewLogHandler.TableMeta tableMeta = tableMetaMap.get(fullTableName);
                synchronized (fullTableName) {
                    if (tableMeta == null) {
                        // 获取表的元数据
                        tableMeta = materializedViewLogHandler.queryTableMeta(fullTableName);
                        tableMetaMap.put(fullTableName, tableMeta);
                    }
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

        };
    }

}
