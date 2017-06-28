package com.github.dataflow.node.model.instance;

import com.github.dataflow.core.instance.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kevin
 * @date 2017-05-30 12:20 AM.
 */
public class InstanceManager {
    private static ConcurrentHashMap<String, Instance> instanceCache = new ConcurrentHashMap<String, Instance>();

    public static void put(String instanceName, Instance instance) {
        instanceCache.putIfAbsent(instanceName, instance);
    }

    public static Instance get(String instanceName) {
        return instanceCache.get(instanceName);
    }

    public static Instance remove(String instanceName) {
        return instanceCache.remove(instanceName);
    }

    public static List<Instance> getAllInstance() {
        return new ArrayList<>(instanceCache.values());
    }
}
