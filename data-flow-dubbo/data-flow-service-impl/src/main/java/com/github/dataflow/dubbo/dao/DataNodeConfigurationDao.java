package com.github.dataflow.dubbo.dao;

import com.github.dataflow.dubbo.model.DataNodeConfiguration;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/5
 */
public interface DataNodeConfigurationDao {
    void insert(DataNodeConfiguration dataNodeConfiguration);

    void update(DataNodeConfiguration dataNodeConfiguration);

    void delete(@Param("id") Long id);

    void deleteBatch(@Param("ids") List<Long> ids);

    DataNodeConfiguration getById(@Param("id") Long id);

    Long countByCondition(DataNodeConfiguration dataNodeConfiguration);

    List<DataNodeConfiguration> findByCondition(DataNodeConfiguration dataNodeConfiguration);

}
