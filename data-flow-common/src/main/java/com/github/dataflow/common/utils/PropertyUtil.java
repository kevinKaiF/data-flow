package com.github.dataflow.common.utils;

import java.util.Properties;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/23
 */
public class PropertyUtil {
    public static boolean getBoolean(Properties properties, String key, String defaultValue) {
        return Boolean.valueOf(properties.getProperty(key, defaultValue));
    }


    public static int getInt(Properties properties, String key, String defaultValue) {
        return Integer.valueOf(properties.getProperty(key, defaultValue));
    }

    public static int getInt(Properties properties, String key, int defaultValue) {
        return Integer.valueOf(properties.getProperty(key, String.valueOf(defaultValue)));
    }


    public static long getLong(Properties properties, String key, String defaultValue) {
        return Long.valueOf(properties.getProperty(key, defaultValue));
    }

    public static long getLong(Properties properties, String key, long defaultValue) {
        return Long.valueOf(properties.getProperty(key, String.valueOf(defaultValue)));
    }


    public static String getString(Properties properties, String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static boolean getBoolean(Properties properties, String key) {
        return Boolean.valueOf(properties.getProperty(key));
    }

    public static int getInt(Properties properties, String key) {
        return Integer.valueOf(properties.getProperty(key));
    }

    public static long getLong(Properties properties, String key) {
        return Long.valueOf(properties.getProperty(key));
    }

    public static String getString(Properties properties, String key) {
        return properties.getProperty(key);
    }

}