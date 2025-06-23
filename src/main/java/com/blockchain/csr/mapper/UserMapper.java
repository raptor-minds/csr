package com.blockchain.csr.mapper;

import com.blockchain.csr.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface User mapper.
 *
 * @author zhangrucheng on 2025/5/18
 */

@Mapper
public interface UserMapper {
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
    int insert(User record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(User record);

    /**
     * Select by primary key user.
     *
     * @param id the id
     * @return the user
     */
    User selectByPrimaryKey(Integer id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(User record);

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     */
    User findByUsername(String username);
}