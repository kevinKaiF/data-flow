package com.github.dataflow.sender.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kevin
 * @date 2017-05-29 11:53 PM.
 */
public class DataSenderManager {
    private static Map<Long, DataSender> dataSenderMap = new HashMap<>();

    public synchronized static void put(Long dataSenderId, DataSender dataSender) {
        // remove the stopped dataSender
        for (DataSender sender : dataSenderMap.values()) {
            if (!sender.isStart()) {
                dataSenderMap.remove(sender.getDataSenderId());
            }
        }

        dataSenderMap.put(dataSenderId, dataSender);
    }

    public synchronized static void remove(Long dataSenderId) {
        dataSenderMap.remove(dataSenderId);
    }

    public synchronized static DataSender get(Long dataSenderId) {
        return dataSenderMap.get(dataSenderId);
    }

    public synchronized static void clear() {
        dataSenderMap.clear();
    }
}
