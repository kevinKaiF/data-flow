package com.github.dataflow.transformer.core.post;

import com.github.dataflow.transformer.exception.DataTransformerException;

import java.lang.reflect.Constructor;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/10/18
 */
class VoidHolder {
    public static final Void returnVoid;

    static {
        try {
            returnVoid = get();
        } catch (Exception e) {
            throw new DataTransformerException(e);
        }
    }

    public static Void get() throws Exception {
        Constructor<Void> declaredConstructor = null;
        try {
            Class<Void> voidClass = Void.class;
            declaredConstructor = voidClass.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance(null);
        } finally {
            if (declaredConstructor != null) {
                declaredConstructor.setAccessible(false);
            }
        }
    }
}
