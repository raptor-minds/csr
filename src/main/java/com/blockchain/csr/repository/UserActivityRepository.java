package com.blockchain.csr.repository;

import com.blockchain.csr.model.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for UserActivity entity
 */
@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Integer> {
    
    /**
     * Find user activities by user ID
     *
     * @param userId the user ID
     * @return List<UserActivity>
     */
    List<UserActivity> findByUserId(Integer userId);
    
    /**
     * Find user activities by activity ID
     *
     * @param activityId the activity ID
     * @return List<UserActivity>
     */
    List<UserActivity> findByActivityId(Integer activityId);
    
    /**
     * Find user activities by state
     *
     * @param state the state
     * @return List<UserActivity>
     */
    List<UserActivity> findByState(String state);
    
    /**
     * Find user activities by user and activity
     *
     * @param userId the user ID
     * @param activityId the activity ID
     * @return List<UserActivity>
     */
    List<UserActivity> findByUserIdAndActivityId(Integer userId, Integer activityId);
} 