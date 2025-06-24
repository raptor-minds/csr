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
     * Reset user password.
     *
     * @param userId the user id
     * @param newPassword the new password
     * @throws IllegalArgumentException if user not found or password is invalid
     */
    void resetPassword(Integer userId, String newPassword);

    /**
     * Get paginated user list with filtering and sorting.
     *
     * @param page the page number (0-based)
     * @param pageSize the page size
     * @param username the username filter (optional)
     * @param sortField the sort field (optional)
     * @param sortOrder the sort order (ascend/descend)
     * @return UserListResponse
     */
    com.blockchain.csr.model.dto.UserListResponse getUserList(Integer page, Integer pageSize, String username, String sortField, String sortOrder);

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

