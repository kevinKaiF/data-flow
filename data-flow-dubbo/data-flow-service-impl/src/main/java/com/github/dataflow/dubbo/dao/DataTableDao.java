package com.github.dataflow.dubbo.dao;

import com.github.dataflow.dubbo.model.DataTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/1
 */
public interface DataTableDao {
    void insert(DataTable dataTable);

    void update(DataTable dataTable);

    void delete(@Param("id") Long id);

    void deleteBatch(@Param("ids") List<Long> ids);

    DataTable getById(@Param("id") Long id);

    Long countByCondition(DataTable dataTable);

    List<DataTable> findByCondition(DataTable dataTable);

    void deleteByDataInstanceId(@Param("dataInstanceId") Long dataInstanceId);
}
