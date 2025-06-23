package com.blockchain.csr.repository;

import com.blockchain.csr.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Role entity
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    /**
     * Find roles by role name
     *
     * @param role the role name
     * @return List<Role>
     */
    List<Role> findByRole(String role);
    
    /**
     * Find roles by activity ID
     *
     * @param activityId the activity ID
     * @return List<Role>
     */
    List<Role> findByActivityId(Integer activityId);
    
    /**
     * Find roles by event name
     *
     * @param eventName the event name
     * @return List<Role>
     */
    List<Role> findByEventName(String eventName);
    
    /**
     * Find roles by activity name
     *
     * @param activityName the activity name
     * @return List<Role>
     */
    List<Role> findByActivityName(String activityName);
} 