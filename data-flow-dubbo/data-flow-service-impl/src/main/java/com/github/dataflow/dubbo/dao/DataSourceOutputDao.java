package com.github.dataflow.dubbo.dao;

import com.github.dataflow.dubbo.model.DataSourceOutput;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/1
 */
public interface DataSourceOutputDao {
    void insert(DataSourceOutput dataSourceOutput);

    void update(DataSourceOutput dataSourceOutput);

    void delete(@Param("id") Long id);

    void deleteBatch(@Param("ids") List<Long> ids);

    DataSourceOutput getById(@Param("id") Long id);

    Long countByCondition(DataSourceOutput dataSourceOutput);

    List<DataSourceOutput> findByCondition(DataSourceOutput dataSourceOutput);
}
