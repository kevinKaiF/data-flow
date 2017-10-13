package com.github.dataflow.common.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/21
 */
public class JSONObjectUtil {
    public static String getString(JSONObject jsonObject, String key, String defaultValue) {
        if (jsonObject.containsKey(key)) {
            return jsonObject.getString(key);
        } else {
            return defaultValue;
        }
    }

    public static String getString(JSONObject jsonObject, String key) {
        return jsonObject.getString(key);
    }

    public static long getLong(JSONObject jsonObject, String key, long defaultValue) {
        if (jsonObject.containsKey(key)) {
            return jsonObject.getLongValue(key);
        } else {
            return defaultValue;
        }
    }

    public static long getLong(JSONObject jsonObject, String key) {
        return jsonObject.getLongValue(key);
    }

    public static int getInt(JSONObject jsonObject, String key, int defaultValue) {
        if (jsonObject.containsKey(key)) {
            return jsonObject.getIntValue(key);
        } else {
            return defaultValue;
        }
    }

    public static int getInt(JSONObject jsonObject, String key) {
        return jsonObject.getIntValue(key);
    }

    public static boolean getBoolean(JSONObject jsonObject, String key, Boolean defaultValue) {
        if (jsonObject.containsKey(key)) {
            return jsonObject.getBooleanValue(key);
        } else {
            return defaultValue;
        }
    }

    public static JSONObject parseJSON(String options) {
        if (options == null || options.trim().length() == 0) {
            return new JSONObject();
        } else {
            return JSONObject.parseObject(options);
        }
    }
}
