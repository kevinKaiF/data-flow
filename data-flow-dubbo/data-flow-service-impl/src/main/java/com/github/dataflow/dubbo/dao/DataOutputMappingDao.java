package com.github.dataflow.dubbo.dao;

import com.github.dataflow.dubbo.model.DataOutputMapping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/1
 */
public interface DataOutputMappingDao {
    void insert(DataOutputMapping dataOutputMapping);

    void update(DataOutputMapping dataOutputMapping);

    void delete(@Param("id") Long id);

    void deleteBatch(@Param("ids") List<Long> ids);

    DataOutputMapping getById(@Param("id") Long id);

    Long countByCondition(DataOutputMapping dataOutputMapping);

    List<DataOutputMapping> findByCondition(DataOutputMapping dataOutputMapping);
}
