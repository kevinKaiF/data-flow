package com.github.dataflow.dubbo.service;

import com.github.dataflow.dubbo.model.DataInstance;
import com.github.dataflow.dubbo.model.PageSet;
import com.github.dataflow.dubbo.model.ServiceResult;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/1
 */
public interface DubboDataInstanceService {
    /**
     * 新增条目
     * <p>
     * note : dataInstance的name必须是唯一的
     *
     * @param dataInstance 带有新增数据的对象
     * @return 返回新增条目数据库影响数
     */
    ServiceResult<Long> insert(DataInstance dataInstance);

    /**
     * 更新条目
     * <p>
     * note : dataInstance的name必须是唯一的
     *
     * @param dataInstance 带有更新数据的对象
     * @return 返回更新条目数据库影响数
     */
    ServiceResult<Integer> update(DataInstance dataInstance);

    /**
     * 根据ID删除对应条目
     *
     * @param id id
     * @return 返回删除条目数据库影响数
     */
    ServiceResult<Integer> delete(Long id);

    /**
     * 批量删除对应条目
     *
     * @param ids ids
     * @return 返回删除条目数据库影响数
     */
    ServiceResult<Integer> deleteBatch(List<Long> ids);

    /**
     * 根据ID查询对应的条目
     *
     * @param id id
     * @return 返回查询的结果
     */
    ServiceResult<DataInstance> getById(Long id);

    /**
     * 根据条件查询对应的条目总数
     *
     * @param dataInstance 带有查询条件的对象
     * @return 返回查询的结果总数
     */
    ServiceResult<Long> countByCondition(DataInstance dataInstance);

    /**
     * 根据条件查询对应的条目
     *
     * @param dataInstance 带有查询条件的对象
     * @param pageSet      分页对象
     * @return 返回查询的结果集合
     */
    ServiceResult<List<DataInstance>> findByCondition(DataInstance dataInstance, PageSet pageSet);

    /**
     * 根据dataInstance名称更新status状态
     *
     * @param name   dataInstance的名称
     * @param status dataInstance的状态
     * @return
     */
    ServiceResult<Void> updateStatusByName(String name, Integer status);

    /**
     * 根据dataInstance id更新status状态
     *
     * @param id
     * @param status
     * @return
     */
    ServiceResult<Void> updateStatusById(Long id, Integer status);

    /**
     * 删除状态为 STOP 的DataInstance
     *
     * @param id
     * @return
     */
    ServiceResult<Void> deleteIfStopped(Long id);

    /**
     * 根据ID查询对应的条目，包括关联的其他表
     *
     * @param id id
     * @return 返回查询的结果
     */
    ServiceResult<DataInstance> getByIdFully(Long id);

    /**
     * 根据条件查询对应的条目，包括关联的其他表
     *
     * @param dataInstance 带有查询条件的对象
     * @param pageSet      分页对象
     * @return 返回查询的结果集合
     */
    ServiceResult<List<DataInstance>> findByConditionFully(DataInstance dataInstance, PageSet pageSet);

}
