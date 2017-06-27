package com.github.dataflow.core.instance.handler;


import com.github.dataflow.core.instance.Instance;
import com.github.dataflow.dubbo.model.DataInstance;

/**
 * @author kevin
 * @date 2017-05-28 3:16 PM.
 */
public interface InstanceHandler {
    boolean support(int instanceType);

    Instance doCreateInstance(DataInstance dataInstance);
}
