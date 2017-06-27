package com.github.dataflow.dubbo.dao;

import com.github.dataflow.dubbo.model.DataUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/7
 */
public interface DataUserDao {
    void insert(DataUser dataUser);

    void update(DataUser dataUser);

    void delete(@Param("id") Long id);

    void deleteBatch(@Param("ids") List<Long> ids);

    DataUser getById(@Param("id") Long id);

    Long countByCondition(DataUser dataUser);

    List<DataUser> findByCondition(DataUser dataUser);

}
