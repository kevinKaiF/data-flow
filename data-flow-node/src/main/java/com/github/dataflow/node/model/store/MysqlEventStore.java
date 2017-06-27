package com.github.dataflow.node.model.store;

import com.alibaba.otter.canal.common.AbstractCanalLifeCycle;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.position.Position;
import com.alibaba.otter.canal.store.CanalEventStore;
import com.alibaba.otter.canal.store.CanalStoreException;
import com.alibaba.otter.canal.store.model.Event;
import com.alibaba.otter.canal.store.model.Events;
import com.github.dataflow.common.model.RowMetaData;
import com.github.dataflow.core.exception.InstanceException;
import com.github.dataflow.core.store.DataStore;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/5/29
 */
public class MysqlEventStore extends AbstractCanalLifeCycle implements CanalEventStore<Event> {
    private DataStore dataStore;

    @Override
    public void start() {
        super.start();
        dataStore.start();
    }

    @Override
    public void stop() {
        super.stop();
        dataStore.stop();
    }

    public void put(List<Event> list) throws InterruptedException, CanalStoreException {
        if (!CollectionUtils.isEmpty(list)) {
            for (Event event : list) {
                CanalEntry.Entry entry = event.getEntry();
                try {
                    CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                    CanalEntry.EventType eventType = rowChange.getEventType();

                    if (eventType == CanalEntry.EventType.INSERT || eventType == CanalEntry.EventType.UPDATE || eventType == CanalEntry.EventType.DELETE) {
                        List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();
                        List<RowMetaData> rowMetaDataList = getRowMetaData(rowDataList, entry, eventType);
                        dataStore.handle(rowMetaDataList);
                    }
                } catch (InvalidProtocolBufferException e) {
                }
            }
        }
    }


    private List<RowMetaData> getRowMetaData(List<CanalEntry.RowData> rowDataList, CanalEntry.Entry entry, CanalEntry.EventType eventType) {
        List<RowMetaData> rowMetaDataList = new ArrayList<RowMetaData>(rowDataList.size());
        RowMetaData.EventType type = parseEventType(eventType);
        for (CanalEntry.RowData rowData : rowDataList) {
            List<RowMetaData.ColumnMeta> beforeColumnMetaList = getColumnMetaList(rowData.getBeforeColumnsList());
            List<RowMetaData.ColumnMeta> afterColumnMetaList = getColumnMetaList(rowData.getAfterColumnsList());
            RowMetaData rowMetaData = new RowMetaData(entry.getHeader().getTableName(),
                                                      entry.getHeader().getSchemaName(),
                                                      type,
                                                      beforeColumnMetaList,
                                                      afterColumnMetaList);
            rowMetaDataList.add(rowMetaData);
        }
        return rowMetaDataList;
    }

    /**
     * 过滤字段
     *
     * @param columnsList
     * @return
     */
    private List<RowMetaData.ColumnMeta> getColumnMetaList(List<CanalEntry.Column> columnsList) {
        List<RowMetaData.ColumnMeta> beforeColumnMetaList = new ArrayList<RowMetaData.ColumnMeta>();
        for (CanalEntry.Column columnMeta : columnsList) {
            beforeColumnMetaList.add(new RowMetaData.ColumnMeta(columnMeta.getName(), columnMeta.getSqlType(), columnMeta.getIsKey(), columnMeta.getValue()));
        }
        return beforeColumnMetaList;
    }

    private RowMetaData.EventType parseEventType(CanalEntry.EventType eventType) {
        switch (eventType) {
            case INSERT:
                return RowMetaData.EventType.INSERT;
            case UPDATE:
                return RowMetaData.EventType.UPDATE;
            case DELETE:
                return RowMetaData.EventType.DELETE;
            default:
                throw new InstanceException("could not parse CanalEntry.EventType [" + eventType + "]");
        }
    }

    public boolean put(List<Event> list, long l, TimeUnit timeUnit) throws InterruptedException, CanalStoreException {
        put(list);
        return true;
    }

    public boolean tryPut(List<Event> list) throws CanalStoreException {
        try {
            put(list);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void put(Event event) throws InterruptedException, CanalStoreException {
        put(Collections.singletonList(event));
    }

    public boolean put(Event event, long l, TimeUnit timeUnit) throws InterruptedException, CanalStoreException {
        put(Collections.singletonList(event));
        return true;
    }

    public boolean tryPut(Event event) throws CanalStoreException {
        try {
            put(Collections.singletonList(event));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    ///////////////////////////////////////////////////////////////////////////////
    //
    //                           忽略canal原有对client订阅
    //
    ///////////////////////////////////////////////////////////////////////////////

    public Events<Event> get(Position position, int i) throws InterruptedException, CanalStoreException {
        return null;
    }

    public Events<Event> get(Position position, int i, long l, TimeUnit timeUnit) throws InterruptedException, CanalStoreException {
        return null;
    }

    public Events<Event> tryGet(Position position, int i) throws CanalStoreException {
        return null;
    }

    public Position getLatestPosition() throws CanalStoreException {
        return null;
    }

    public Position getFirstPosition() throws CanalStoreException {
        return null;
    }

    public void ack(Position position) throws CanalStoreException {

    }

    public void rollback() throws CanalStoreException {

    }

    public void cleanUntil(Position position) throws CanalStoreException {

    }

    public void cleanAll() throws CanalStoreException {

    }
}
