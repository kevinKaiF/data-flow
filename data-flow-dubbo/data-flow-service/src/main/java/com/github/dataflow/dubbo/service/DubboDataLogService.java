package com.github.dataflow.dubbo.service;

import com.github.dataflow.dubbo.model.DataLog;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/5
 */
public interface DubboDataLogService {
    /**
     * 新增条目
     *
     * @param dataLog 带有新增数据的对象
     * @return 返回新增条目数据库影响数
     */
    ServiceResult<Long> insert(DataLog dataLog);

    /**
     * 更新条目
     *
     * @param dataLog 带有更新数据的对象
     * @return 返回更新条目数据库影响数
     */
    ServiceResult<Integer> update(DataLog dataLog);

    /**
     * 根据ID删除对应条目
     *
     * @param id id
     * @return 返回删除条目数据库影响数
     */
    ServiceResult<Integer> delete(Long id);

    /**
     * 根据ID查询对应的条目
     *
     * @param id id
     * @return 返回查询的结果
     */
    ServiceResult<DataLog> getById(Long id);

    /**
     * 根据条件查询对应的条目总数
     *
     * @param dataLog 带有查询条件的对象
     * @return 返回查询的结果总数
     */
    ServiceResult<Long> countByCondition(DataLog dataLog);

    /**
     * 根据条件查询对应的条目，不包含message
     *
     * @param dataLog 带有查询条件的对象
     * @param pageSet 分页对象
     * @return 返回查询的结果集合
     */
    ServiceResult<List<DataLog>> findByCondition(DataLog dataLog, PageSet pageSet);


    /**
     * 根据条件查询对应的条目
     *
     * @param dataLog 带有查询条件的对象
     * @param pageSet 分页对象
     * @return 返回查询的结果集合
     */
    ServiceResult<List<DataLog>> findByConditionFully(DataLog dataLog, PageSet pageSet);

    /**
     * 根据条件删除
     *
     * @param dataLog
     * @return
     */
    ServiceResult<Void> deleteByCondition(DataLog dataLog);
}
