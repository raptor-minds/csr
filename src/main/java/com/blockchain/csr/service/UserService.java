package com.blockchain.csr.service;

import com.blockchain.csr.model.dto.UserDto;
import com.blockchain.csr.model.dto.UserListResponse;
import com.blockchain.csr.model.entity.User;
import java.util.List;

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
    UserListResponse getUserList(Integer page, Integer pageSize, String username, String sortField, String sortOrder);

    /**
     * Get user details by ID.
     *
     * @param id the user ID
     * @return UserDto with user details including event and activity counts
     * @throws IllegalArgumentException if user not found
     */
    UserDto getUserDetails(Integer id);

    /**
     * Update user information.
     *
     * @param id the user ID
     * @param updateRequest the update request containing username, role, and location
     * @throws IllegalArgumentException if user not found or username already exists
     */
    void updateUser(Integer id, com.blockchain.csr.model.dto.UserUpdateRequest updateRequest);

    /**
     * Change the reviewer for a user.
     *
     * @param userId the user ID of the user to be updated
     * @param reviewerId the user ID of the new reviewer
     * @throws IllegalArgumentException if user or reviewer not found, or if user is an admin
     */
    void changeReviewer(Integer userId, Integer reviewerId);

    /**
     * Batch delete users by their IDs.
     *
     * @param userIds the list of user IDs to delete
     * @throws IllegalArgumentException if any user ID is invalid
     */
    void batchDeleteUsers(List<Integer> userIds);

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

