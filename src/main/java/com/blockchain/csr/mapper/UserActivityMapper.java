package com.blockchain.csr.mapper;

import com.blockchain.csr.model.entity.UserActivity;

/**
 * The interface User activity mapper.
 *
 * @author zhangrucheng on 2025/5/19
 */
public interface UserActivityMapper {
    /**
     * Delete by primary key int.
     *
     * @param id the id
     * @return the int
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * Insert int.
     *
     * @param record the record
     * @return the int
     */
    int insert(UserActivity record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(UserActivity record);

    /**
     * Select by primary key user activity.
     *
     * @param id the id
     * @return the user activity
     */
    UserActivity selectByPrimaryKey(Integer id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(UserActivity record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(UserActivity record);
}