package com.github.dataflow.node.model.instance.handler;


import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.node.model.instance.Instance;

/**
 * @author kevin
 * @date 2017-05-28 3:16 PM.
 */
public interface InstanceHandler {
    boolean support(int instanceType);

    Instance doCreateInstance(DataInstance dataInstance);
}
