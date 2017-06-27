package com.github.dataflow.dashboard.controller;

import com.github.dataflow.common.model.ResponseEntity;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/8
 */
public abstract class Callable {
    private Object[] params;

    public Callable(Object... params) {
        this.params = params;
    }

    public abstract void call(ResponseEntity responseEntity);

    public Object[] getParams() {
        return params;
    }
}
