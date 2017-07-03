package com.github.dataflow.node.model.instance.mysql;

import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.github.dataflow.core.alarm.AlarmService;
import com.github.dataflow.core.instance.AbstractInstance;
import com.github.dataflow.core.store.DataStore;

/**
 * @author kevin
 * @date 2017-05-28 3:17 PM.
 */
public class MysqlInstance extends AbstractInstance {
    private MysqlInstanceDelegate mysqlInstanceDelegate;

    public MysqlInstance() {
        this.mysqlInstanceDelegate = new MysqlInstanceDelegate();
    }

    @Override
    public void doStart() {
        mysqlInstanceDelegate.start();
    }

    @Override
    public void doStop() {
        mysqlInstanceDelegate.stop();
    }

    public CanalParameter getCanalParameter() {
        return mysqlInstanceDelegate.getCanalParameter();
    }

    public void setCanalParameter(CanalParameter canalParameter) {
        mysqlInstanceDelegate.setCanalParameter(canalParameter);
    }

    @Override
    public String getPosition(String instanceName) {
        return mysqlInstanceDelegate.getPosition(instanceName);
    }

    @Override
    public Long getId() {
        return mysqlInstanceDelegate.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
        mysqlInstanceDelegate.setId(id);
    }

    @Override
    public String getName() {
        return mysqlInstanceDelegate.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        mysqlInstanceDelegate.setName(name);
    }

    @Override
    public String getWhiteFilter() {
        return mysqlInstanceDelegate.getWhiteFilter();
    }

    @Override
    public void setWhiteFilter(String whiteFilter) {
        super.setWhiteFilter(whiteFilter);
        mysqlInstanceDelegate.setWhiteFilter(whiteFilter);
    }

    @Override
    public void setAlarmService(AlarmService alarmService) {
        super.setAlarmService(alarmService);
        mysqlInstanceDelegate.setAlarmService(alarmService);
    }

    @Override
    public void setDataStore(DataStore dataStore) {
        super.setDataStore(dataStore);
        mysqlInstanceDelegate.setDataStore(dataStore);
    }

    @Override
    protected void doInit() {
        mysqlInstanceDelegate.init();
    }
}
