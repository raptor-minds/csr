package com.blockchain.csr.mapper;

import com.blockchain.csr.model.entity.Role;

/**
 * The interface Role mapper.
 *
 * @author zhangrucheng on 2025/5/19
 */
public interface RoleMapper {
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
    int insert(Role record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(Role record);

    /**
     * Select by primary key role.
     *
     * @param id the id
     * @return the role
     */
    Role selectByPrimaryKey(Integer id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(Role record);
}