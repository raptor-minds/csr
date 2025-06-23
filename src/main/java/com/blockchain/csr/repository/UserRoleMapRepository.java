package com.blockchain.csr.repository;

import com.blockchain.csr.model.entity.UserRoleMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for UserRoleMap entity
 */
@Repository
public interface UserRoleMapRepository extends JpaRepository<UserRoleMap, Integer> {
    
    /**
     * Find user role maps by user ID
     *
     * @param userId the user ID
     * @return List<UserRoleMap>
     */
    List<UserRoleMap> findByUserId(Integer userId);
    
    /**
     * Find user role maps by role ID
     *
     * @param roleId the role ID
     * @return List<UserRoleMap>
     */
    List<UserRoleMap> findByRoleId(Integer roleId);
    
    /**
     * Find user role map by user and role
     *
     * @param userId the user ID
     * @param roleId the role ID
     * @return UserRoleMap
     */
    UserRoleMap findByUserIdAndRoleId(Integer userId, Integer roleId);
    
    /**
     * Check if user role map exists
     *
     * @param userId the user ID
     * @param roleId the role ID
     * @return boolean
     */
    boolean existsByUserIdAndRoleId(Integer userId, Integer roleId);
    
    /**
     * Delete by user ID and role ID
     *
     * @param userId the user ID
     * @param roleId the role ID
     */
    void deleteByUserIdAndRoleId(Integer userId, Integer roleId);
} 