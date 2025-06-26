package com.blockchain.csr.repository;

import com.blockchain.csr.model.entity.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for UserEvent entity
 */
@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Integer> {
    
    /**
     * Find user events by user ID
     *
     * @param userId the user ID
     * @return List<UserEvent>
     */
    List<UserEvent> findByUserId(Integer userId);
    
    /**
     * Find user events by event ID
     *
     * @param eventId the event ID
     * @return List<UserEvent>
     */
    List<UserEvent> findByEventId(Integer eventId);
    
    /**
     * Find user event by user and event
     *
     * @param userId the user ID
     * @param eventId the event ID
     * @return UserEvent
     */
    UserEvent findByUserIdAndEventId(Integer userId, Integer eventId);
    
    /**
     * Check if user event exists
     *
     * @param userId the user ID
     * @param eventId the event ID
     * @return boolean
     */
    boolean existsByUserIdAndEventId(Integer userId, Integer eventId);

    @Query("SELECT e.id, e.name, e.type, e.totalTime, e.endTime, e.status FROM Event e " +
           "JOIN UserEvent ue ON e.id = ue.eventId " +
           "WHERE ue.userId = :userId")
    List<Object[]> findUserEventsWithInfo(@Param("userId") Integer userId);
} 