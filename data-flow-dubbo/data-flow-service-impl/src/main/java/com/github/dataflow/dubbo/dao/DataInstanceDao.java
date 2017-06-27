package com.github.dataflow.dubbo.dao;

import com.github.dataflow.dubbo.model.DataInstance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/1
 */
public interface DataInstanceDao {
    void insert(DataInstance dataInstance);

    void update(DataInstance dataInstance);

    void delete(@Param("id") Long id);

    void deleteBatch(@Param("ids") List<Long> ids);

    DataInstance getById(@Param("id") Long id);

    Long countByCondition(DataInstance dataInstance);

    List<DataInstance> findByCondition(DataInstance dataInstance);

    void updateStatusByName(@Param("name") String name, @Param("status") Integer status);

    void deleteIfStopped(@Param("id") Long id);

    DataInstance getByIdFully(@Param("id") Long id);

    List<DataInstance> findByConditionFully(DataInstance dataInstance);

    void updateStatusById(@Param("id") Long id, @Param("status") Integer status);

    List<DataInstance> getByName(@Param("name") String instanceName);
}
