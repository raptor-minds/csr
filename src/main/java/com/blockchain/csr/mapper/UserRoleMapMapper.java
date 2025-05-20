package com.blockchain.csr.mapper;

import com.blockchain.csr.model.entity.UserRoleMap;

/**
 * The interface User role map mapper.
 *
 * @author zhangrucheng on 2025/5/19
 */
public interface UserRoleMapMapper {
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
    int insert(UserRoleMap record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(UserRoleMap record);

    /**
     * Select by primary key user role map.
     *
     * @param id the id
     * @return the user role map
     */
    UserRoleMap selectByPrimaryKey(Integer id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(UserRoleMap record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(UserRoleMap record);
}