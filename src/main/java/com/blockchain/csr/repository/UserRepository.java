package com.blockchain.csr.repository;

import com.blockchain.csr.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for User entity
 *
 * @author zhangrucheng on 2025/5/18
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    
    /**
     * Find user by username
     *
     * @param username the username
     * @return Optional<User>
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Check if user exists by username
     *
     * @param username the username
     * @return boolean
     */
    boolean existsByUsername(String username);
    
    /**
     * Get event count for a user
     *
     * @param userId the user id
     * @return event count
     */
    @Query("SELECT COUNT(ue) FROM UserEvent ue WHERE ue.userId = :userId")
    Integer getEventCountByUserId(@Param("userId") Integer userId);
    
    /**
     * Get activity count for a user
     *
     * @param userId the user id
     * @return activity count
     */
    @Query("SELECT COUNT(ua) FROM UserActivity ua WHERE ua.userId = :userId")
    Integer getActivityCountByUserId(@Param("userId") Integer userId);
} 