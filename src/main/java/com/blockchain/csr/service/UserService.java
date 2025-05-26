package com.blockchain.csr.service;

import com.blockchain.csr.model.entity.User;

/**
 * The interface User service.
 *
 * @author zhangrucheng on 2025/5/18
 */
public interface UserService {

    /**
     * Create user.
     *
     * @param userName the user name
     * @param password the password
     * @throws IllegalArgumentException if user with the same username already exists
     */
    void createUser(String userName, String password);

    /**
     * Check if user exists by username.
     *
     * @param username the username
     * @return true if user exists, false otherwise
     */
    boolean existsByUsername(String username);

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
}

