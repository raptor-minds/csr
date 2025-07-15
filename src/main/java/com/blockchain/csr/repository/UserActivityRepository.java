package com.blockchain.csr.repository;

import com.blockchain.csr.model.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT ua FROM UserActivity ua WHERE ua.userId = :userId AND (ua.deleted = false OR ua.deleted IS NULL)")
    List<UserActivity> findByUserId(Integer userId);
    
    /**
     * Find user activities by user and activity
     *
     * @param userId the user ID
     * @param activityId the activity ID
     * @return List<UserActivity>
     */
    @Query("SELECT ua FROM UserActivity ua WHERE ua.userId = :userId and ua.activityId = :activityId AND (ua.deleted = false OR ua.deleted IS NULL)")
    List<UserActivity> findByUserIdAndActivityId(Integer userId, Integer activityId);
    
    /**
     * Check if user activity exists by user ID and activity ID
     *
     * @param userId the user ID
     * @param activityId the activity ID
     * @return true if exists, false otherwise
     */
    boolean existsByUserIdAndActivityId(Integer userId, Integer activityId);
    
    /**
     * Find user activities by multiple activity IDs and state (excluding deleted)
     *
     * @param activityIds the list of activity IDs
     * @param state the state
     * @return List<UserActivity>
     */
    @Query("SELECT ua FROM UserActivity ua WHERE ua.activityId IN :activityIds AND ua.state = :state AND (ua.deleted = false OR ua.deleted IS NULL)")
    List<UserActivity> findByActivityIdInAndState(@Param("activityIds") List<Integer> activityIds, @Param("state") String state);
    
    /**
     * Count participants with SIGNED_UP state for a specific activity (excluding deleted)
     *
     * @param activityId the activity ID
     * @return number of signed up participants
     */
    @Query("SELECT COUNT(ua) FROM UserActivity ua WHERE ua.activityId = :activityId AND ua.state = 'SIGNED_UP' AND (ua.deleted = false OR ua.deleted IS NULL)")
    Integer countSignedUpParticipantsByActivityId(@Param("activityId") Integer activityId);
    
    /**
     * Count unique participants with SIGNED_UP state for all activities in an event (de-duplicated, excluding deleted)
     *
     * @param eventId the event ID
     * @return number of unique signed up participants across all activities in the event
     */
    @Query("SELECT COUNT(DISTINCT ua.userId) FROM UserActivity ua " +
           "JOIN Activity a ON ua.activityId = a.id " +
           "WHERE a.eventId = :eventId AND ua.state = 'SIGNED_UP' AND (ua.deleted = false OR ua.deleted IS NULL)")
    Integer countUniqueSignedUpParticipantsByEventId(@Param("eventId") Integer eventId);

    /**
     * Find user activities by userId and eventId
     * @param userId 用户ID
     * @param eventId 事件ID
     * @return List<UserActivity>
     */
    @Query("SELECT ua FROM UserActivity ua JOIN Activity a ON ua.activityId = a.id WHERE ua.userId = :userId AND a.eventId = :eventId")
    List<UserActivity> findByUserIdAndEventId(@Param("userId") Integer userId, @Param("eventId") Integer eventId);

    /**
     * Find the latest user activity by userId and activityId (by createdAt desc)
     */
    UserActivity findTopByUserIdAndActivityIdOrderByCreatedAtDesc(Integer userId, Integer activityId);
} 