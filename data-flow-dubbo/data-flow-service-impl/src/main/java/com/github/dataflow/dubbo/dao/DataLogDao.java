package com.github.dataflow.dubbo.dao;

import com.github.dataflow.dubbo.model.DataLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/5
 */
public interface DataLogDao {
    void insert(DataLog dataLog);

    void update(DataLog dataLog);

    void delete(@Param("id") Long id);

    DataLog getById(@Param("id") Long id);

    Long countByCondition(DataLog dataLog);

    List<DataLog> findByCondition(DataLog dataLog);

    List<DataLog> findByConditionFully(DataLog dataLog);
}
