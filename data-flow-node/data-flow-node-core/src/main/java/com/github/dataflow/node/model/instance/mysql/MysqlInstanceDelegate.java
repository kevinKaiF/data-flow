package com.github.dataflow.node.model.instance.mysql;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.common.CanalException;
import com.alibaba.otter.canal.common.utils.JsonUtils;
import com.alibaba.otter.canal.common.zookeeper.ZkClientx;
import com.alibaba.otter.canal.filter.aviater.AviaterRegexFilter;
import com.alibaba.otter.canal.instance.core.AbstractCanalInstance;
import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.alibaba.otter.canal.meta.MemoryMetaManager;
import com.alibaba.otter.canal.meta.PeriodMixedMetaManager;
import com.alibaba.otter.canal.meta.ZooKeeperMetaManager;
import com.alibaba.otter.canal.parse.CanalEventParser;
import com.alibaba.otter.canal.parse.ha.CanalHAController;
import com.alibaba.otter.canal.parse.ha.HeartBeatHAController;
import com.alibaba.otter.canal.parse.inbound.AbstractEventParser;
import com.alibaba.otter.canal.parse.inbound.group.GroupEventParser;
import com.alibaba.otter.canal.parse.inbound.mysql.LocalBinlogEventParser;
import com.alibaba.otter.canal.parse.inbound.mysql.MysqlEventParser;
import com.alibaba.otter.canal.parse.index.*;
import com.alibaba.otter.canal.parse.support.AuthenticationInfo;
import com.alibaba.otter.canal.protocol.position.EntryPosition;
import com.alibaba.otter.canal.sink.entry.EntryEventSink;
import com.alibaba.otter.canal.sink.entry.group.GroupEventSink;
import com.github.dataflow.core.alarm.AlarmService;
import com.github.dataflow.core.instance.Instance;
import com.github.dataflow.core.store.DataStore;
import com.github.dataflow.node.model.alarm.AlarmServiceDelegate;
import com.github.dataflow.node.model.store.MysqlEventStore;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author kevin
 * @date 2017-05-28 3:17 PM.
 */
public class MysqlInstanceDelegate extends AbstractCanalInstance implements Instance {
    private static Logger logger = LoggerFactory.getLogger(MysqlInstanceDelegate.class);

    private Long id;

    private String name;

    private CanalParameter canalParameter;

    private String whiteFilter;

    private AlarmService alarmService;

    private DataStore dataStore;

    private CanalLogPositionManager logPositionManager;

    public MysqlInstanceDelegate() {
    }

    public String getName() {
        return name;
    }

    public void setName(String instanceName) {
        this.name = instanceName;
    }

    public void start() {
        logger.info("start MysqlInstance for {} / {} with parameters:{}", new Object[]{this.id, this.name, this.canalParameter});
        if (this.running) {
            throw new CanalException(this.getClass().getName() + " has started , don\'t repeat start");
        } else {
            this.running = true;
        }

        if (!this.metaManager.isStart()) {
            this.metaManager.start();
        }

        if (!this.alarmHandler.isStart()) {
            this.alarmHandler.start();
        }

        if (!this.eventStore.isStart()) {
            this.eventStore.start();
        }

        if (!this.eventSink.isStart()) {
            this.eventSink.start();
        }

        if (!this.eventParser.isStart()) {
            this.beforeStartEventParser(this.eventParser);
            this.eventParser.start();
            this.afterStartEventParser(this.eventParser);
        }

        logger.info("start MysqlInstance successful...");
    }

    public void stop() {
        if (!this.running) {
            throw new CanalException(this.getClass().getName() + " isn\'t start , please check");
        } else {
            this.running = false;
        }

        logger.info("stop MysqlInstance for {} / {} ", new Object[]{this.id, this.name});
        if (this.eventParser.isStart()) {
            this.beforeStopEventParser(this.eventParser);
            this.eventParser.stop();
            this.afterStopEventParser(this.eventParser);
        }

        if (this.eventSink.isStart()) {
            this.eventSink.stop();
        }

        if (this.eventStore.isStart()) {
            this.eventStore.stop();
        }

        if (this.metaManager.isStart()) {
            this.metaManager.stop();
        }

//        if (this.alarmHandler.isStart()) {
//            this.alarmHandler.stop();
//        }

        logger.info("stop MysqlInstance successful...");
    }

    public void init() {
        logger.info("init MysqlInstance for {}-{} with parameters:{}", new Object[]{this.id, this.name, this.canalParameter});
        this.initAlarmHandler();
        this.initMetaManager();
        this.initEventStore();
        this.initEventSink();
        this.initEventParser();
    }

    protected void initAlarmHandler() {
        logger.info("init alarmHandler begin...");
        AlarmServiceDelegate alarmMailService = new AlarmServiceDelegate();
        alarmMailService.setAlarmService(alarmService);
        alarmHandler = alarmMailService;
        logger.info("init alarmHandler end! \n\t load MysqlAlarmHandler:{} ", alarmHandler.getClass().getName());
    }

    protected void initMetaManager() {
        logger.info("init metaManager begin...");
        CanalParameter.MetaMode mode = canalParameter.getMetaMode();
        if (mode.isMemory()) {
            metaManager = new MemoryMetaManager();
        } else if (mode.isZookeeper()) {
            metaManager = new ZooKeeperMetaManager();
            ((ZooKeeperMetaManager) metaManager).setZkClientx(getZkclientx());
        } else if (mode.isMixed()) {
            // metaManager = new MixedMetaManager();
            metaManager = new PeriodMixedMetaManager();// 换用优化过的mixed, at
            // 2012-09-11
            // 设置内嵌的zk metaManager
            ZooKeeperMetaManager zooKeeperMetaManager = new ZooKeeperMetaManager();
            zooKeeperMetaManager.setZkClientx(getZkclientx());
            ((PeriodMixedMetaManager) metaManager).setZooKeeperMetaManager(zooKeeperMetaManager);
        } else {
            throw new CanalException("unsupport MetaMode for " + mode);
        }

        logger.info("init metaManager end! \n\t load MysqlMetaManager:{} ", metaManager.getClass().getName());
    }

    protected void initEventStore() {
        logger.info("init eventStore begin...");
        MysqlEventStore mysqlEventStore = new MysqlEventStore();
        mysqlEventStore.setDataStore(dataStore);
        mysqlEventStore.setAddress(canalParameter.getDbAddresses().get(0));
        mysqlEventStore.setName(name);
        mysqlEventStore.setCanalLogPositionManager(initLogPositionManager());
        eventStore = mysqlEventStore;
        logger.info("init eventStore end! \n\t load MysqlEventStore:{}", eventStore.getClass().getName());
    }


    protected void initEventSink() {
        logger.info("init eventSink begin...");

        int groupSize = getGroupSize();
        if (groupSize <= 1) {
            eventSink = new EntryEventSink();
        } else {
            eventSink = new GroupEventSink(groupSize);
        }

        if (eventSink instanceof EntryEventSink) {
            ((EntryEventSink) eventSink).setFilterTransactionEntry(false);
            ((EntryEventSink) eventSink).setEventStore(getEventStore());
        }
        logger.info("init eventSink end! \n\t load MysqlEventSink:{}", eventSink.getClass().getName());
    }

    private int getGroupSize() {
        List groupDbAddresses = this.canalParameter.getGroupDbAddresses();
        return !CollectionUtils.isEmpty(groupDbAddresses) ? ((List) groupDbAddresses.get(0)).size() : 1;
    }

    protected void initEventParser() {
        logger.info("init eventParser begin...");
        CanalParameter.SourcingType type = canalParameter.getSourcingType();

        List<List<CanalParameter.DataSourcing>> groupDbAddresses = canalParameter.getGroupDbAddresses();
        if (!CollectionUtils.isEmpty(groupDbAddresses)) {
            int size = groupDbAddresses.get(0).size();// 取第一个分组的数量，主备分组的数量必须一致
            List<CanalEventParser> eventParsers = new ArrayList<CanalEventParser>();
            for (int i = 0; i < size; i++) {
                List<InetSocketAddress> dbAddress = new ArrayList<InetSocketAddress>();
                CanalParameter.SourcingType lastType = null;
                for (List<CanalParameter.DataSourcing> groupDbAddress : groupDbAddresses) {
                    if (lastType != null && !lastType.equals(groupDbAddress.get(i).getType())) {
                        throw new CanalException(String.format("master/slave Sourcing type is unmatch. %s vs %s",
                                                               lastType,
                                                               groupDbAddress.get(i).getType()));
                    }

                    lastType = groupDbAddress.get(i).getType();
                    dbAddress.add(groupDbAddress.get(i).getDbAddress());
                }

                // 初始化其中的一个分组parser
                eventParsers.add(doInitEventParser(lastType, dbAddress));
            }

            if (eventParsers.size() > 1) { // 如果存在分组，构造分组的parser
                GroupEventParser groupEventParser = new GroupEventParser();
                groupEventParser.setEventParsers(eventParsers);
                this.eventParser = groupEventParser;
            } else {
                this.eventParser = eventParsers.get(0);
            }
        } else {
            // 创建一个空数据库地址的parser，可能使用了tddl指定地址，启动的时候才会从tddl获取地址
            this.eventParser = doInitEventParser(type, new ArrayList<InetSocketAddress>());
        }

        logger.info("init eventParser end! \n\t load MysqlEventParser:{}", eventParser.getClass().getName());
    }

    private CanalEventParser doInitEventParser(CanalParameter.SourcingType type, List<InetSocketAddress> dbAddresses) {
        CanalEventParser eventParser;
        if (type.isMysql()) {
            MysqlEventParser mysqlEventParser = new MysqlEventParser();
            mysqlEventParser.setDestination(name);
            // 编码参数
            mysqlEventParser.setConnectionCharset(Charset.forName(canalParameter.getConnectionCharset()));
            mysqlEventParser.setConnectionCharsetNumber(canalParameter.getConnectionCharsetNumber());
            // 网络相关参数
            mysqlEventParser.setDefaultConnectionTimeoutInSeconds(canalParameter.getDefaultConnectionTimeoutInSeconds());
            mysqlEventParser.setSendBufferSize(canalParameter.getSendBufferSize());
            mysqlEventParser.setReceiveBufferSize(canalParameter.getReceiveBufferSize());
            // 心跳检查参数
            mysqlEventParser.setDetectingEnable(canalParameter.getDetectingEnable());
            mysqlEventParser.setDetectingSQL(canalParameter.getDetectingSQL());
            mysqlEventParser.setDetectingIntervalInSeconds(canalParameter.getDetectingIntervalInSeconds());
            // 数据库信息参数
            mysqlEventParser.setSlaveId(canalParameter.getSlaveId());
            if (!CollectionUtils.isEmpty(dbAddresses)) {
                mysqlEventParser.setMasterInfo(new AuthenticationInfo(dbAddresses.get(0),
                                                                      canalParameter.getDbUsername(),
                                                                      canalParameter.getDbPassword(),
                                                                      canalParameter.getDefaultDatabaseName()));

                if (dbAddresses.size() > 1) {
                    mysqlEventParser.setStandbyInfo(new AuthenticationInfo(dbAddresses.get(1),
                                                                           canalParameter.getDbUsername(),
                                                                           canalParameter.getDbPassword(),
                                                                           canalParameter.getDefaultDatabaseName()));
                }
            }

            if (!CollectionUtils.isEmpty(canalParameter.getPositions())) {
                EntryPosition masterPosition = JsonUtils.unmarshalFromString(canalParameter.getPositions().get(0),
                                                                             EntryPosition.class);
                // binlog位置参数
                mysqlEventParser.setMasterPosition(masterPosition);

                if (canalParameter.getPositions().size() > 1) {
                    EntryPosition standbyPosition = JsonUtils.unmarshalFromString(canalParameter.getPositions().get(0),
                                                                                  EntryPosition.class);
                    mysqlEventParser.setStandbyPosition(standbyPosition);
                }
            }
            mysqlEventParser.setFallbackIntervalInSeconds(canalParameter.getFallbackIntervalInSeconds());
            mysqlEventParser.setProfilingEnabled(false);
            mysqlEventParser.setFilterTableError(canalParameter.getFilterTableError());
            eventParser = mysqlEventParser;
        } else if (type.isLocalBinlog()) {
            LocalBinlogEventParser localBinlogEventParser = new LocalBinlogEventParser();
            localBinlogEventParser.setDestination(name);
            localBinlogEventParser.setBufferSize(canalParameter.getReceiveBufferSize());
            localBinlogEventParser.setConnectionCharset(Charset.forName(canalParameter.getConnectionCharset()));
            localBinlogEventParser.setConnectionCharsetNumber(canalParameter.getConnectionCharsetNumber());
            localBinlogEventParser.setDirectory(canalParameter.getLocalBinlogDirectory());
            localBinlogEventParser.setProfilingEnabled(false);
            localBinlogEventParser.setDetectingEnable(canalParameter.getDetectingEnable());
            localBinlogEventParser.setDetectingIntervalInSeconds(canalParameter.getDetectingIntervalInSeconds());
            localBinlogEventParser.setFilterTableError(canalParameter.getFilterTableError());
            // 数据库信息，反查表结构时需要
            if (!CollectionUtils.isEmpty(dbAddresses)) {
                localBinlogEventParser.setMasterInfo(new AuthenticationInfo(dbAddresses.get(0),
                                                                            canalParameter.getDbUsername(),
                                                                            canalParameter.getDbPassword(),
                                                                            canalParameter.getDefaultDatabaseName()));

            }
            eventParser = localBinlogEventParser;
        } else if (type.isOracle()) {
            throw new CanalException("unsupport SourcingType for " + type);
        } else {
            throw new CanalException("unsupport SourcingType for " + type);
        }

        // add transaction support at 2012-12-06
        if (eventParser instanceof AbstractEventParser) {
            AbstractEventParser abstractEventParser = (AbstractEventParser) eventParser;
            abstractEventParser.setTransactionSize(canalParameter.getTransactionSize());
            abstractEventParser.setLogPositionManager(initLogPositionManager());
            abstractEventParser.setAlarmHandler(getAlarmHandler());
            abstractEventParser.setEventSink(getEventSink());

            if (StringUtils.isNotEmpty(whiteFilter)) {
                AviaterRegexFilter aviaterFilter = new AviaterRegexFilter(whiteFilter);
                abstractEventParser.setEventFilter(aviaterFilter);
            }

            // 设置黑名单
            if (StringUtils.isNotEmpty(canalParameter.getBlackFilter())) {
                AviaterRegexFilter aviaterFilter = new AviaterRegexFilter(canalParameter.getBlackFilter());
                abstractEventParser.setEventBlackFilter(aviaterFilter);
            }
        }
        if (eventParser instanceof MysqlEventParser) {
            MysqlEventParser mysqlEventParser = (MysqlEventParser) eventParser;

            // 初始化haController，绑定与eventParser的关系，haController会控制eventParser
            CanalHAController haController = initHaController();
            mysqlEventParser.setHaController(haController);
        }
        return eventParser;
    }

    protected CanalHAController initHaController() {
        logger.info("init haController begin...");
        CanalParameter.HAMode haMode = canalParameter.getHaMode();
        CanalHAController haController = null;
        if (haMode.isHeartBeat()) {
            haController = new HeartBeatHAController();
            ((HeartBeatHAController) haController).setDetectingRetryTimes(canalParameter.getDetectingRetryTimes());
            ((HeartBeatHAController) haController).setSwitchEnable(canalParameter.getHeartbeatHaEnable());
        } else {
            throw new CanalException("unsupport HAMode for " + haMode);
        }
        logger.info("init haController end! \n\t load MysqlHAController:{}", haController.getClass().getName());

        return haController;
    }

    protected CanalLogPositionManager initLogPositionManager() {
        if (logPositionManager != null) {
            return logPositionManager;
        }
        logger.info("init logPositionPersistManager begin...");
        CanalParameter.IndexMode indexMode = canalParameter.getIndexMode();
        if (indexMode.isMemory()) {
            logPositionManager = new MemoryLogPositionManager();
        } else if (indexMode.isZookeeper()) {
            logPositionManager = new ZooKeeperLogPositionManager();
            ((ZooKeeperLogPositionManager) logPositionManager).setZkClientx(getZkclientx());
        } else if (indexMode.isMixed()) {
            logPositionManager = new PeriodMixedLogPositionManager();

            ZooKeeperLogPositionManager zooKeeperLogPositionManager = new ZooKeeperLogPositionManager();
            zooKeeperLogPositionManager.setZkClientx(getZkclientx());
            ((PeriodMixedLogPositionManager) logPositionManager).setZooKeeperLogPositionManager(zooKeeperLogPositionManager);
        } else if (indexMode.isMeta()) {
            logPositionManager = new MetaLogPositionManager();
            ((MetaLogPositionManager) logPositionManager).setMetaManager(metaManager);
        } else if (indexMode.isMemoryMetaFailback()) {
            MemoryLogPositionManager primaryLogPositionManager = new MemoryLogPositionManager();
            MetaLogPositionManager failbackLogPositionManager = new MetaLogPositionManager();
            failbackLogPositionManager.setMetaManager(metaManager);

            logPositionManager = new FailbackLogPositionManager();
            ((FailbackLogPositionManager) logPositionManager).setPrimary(primaryLogPositionManager);
            ((FailbackLogPositionManager) logPositionManager).setFailback(failbackLogPositionManager);
        } else {
            throw new CanalException("unsupport indexMode for " + indexMode);
        }

        logger.info("init logPositionManager end! \n\t load MysqlLogPositionManager:{}", logPositionManager.getClass()
                .getName());

        return logPositionManager;
    }

    private synchronized ZkClientx getZkclientx() {
        // 做一下排序，保证相同的机器只使用同一个链接
        List<String> zkClusters = new ArrayList<String>(canalParameter.getZkClusters());
        Collections.sort(zkClusters);

        return ZkClientx.getZkClient(StringUtils.join(zkClusters, ";"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CanalParameter getCanalParameter() {
        return canalParameter;
    }

    public void setCanalParameter(CanalParameter canalParameter) {
        this.canalParameter = canalParameter;
    }

    public String getWhiteFilter() {
        return whiteFilter;
    }

    public void setWhiteFilter(String whiteFilter) {
        this.whiteFilter = whiteFilter;
    }

    @Override
    public void setAlarmService(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    @Override
    public String getPosition(String instanceName) {
        if (eventParser instanceof MysqlEventParser) {
            CanalLogPositionManager logPositionManager = ((MysqlEventParser) this.eventParser).getLogPositionManager();
            return JSON.toJSONString(logPositionManager.getLatestIndexBy(instanceName));
        } else {
            return null;
        }
    }
}
