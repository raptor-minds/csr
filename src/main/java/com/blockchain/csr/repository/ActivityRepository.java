package com.blockchain.csr.repository;

import com.blockchain.csr.model.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Activity entity
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    
    /**
     * Find activities by event ID
     *
     * @param eventId the event ID
     * @return List<Activity>
     */
    List<Activity> findByEventId(Integer eventId);
    
    /**
     * Find activities by template ID
     *
     * @param templateId the template ID
     * @return List<Activity>
     */
    List<Activity> findByTemplateId(Integer templateId);
    
    /**
     * Find activities by name containing
     *
     * @param name the name
     * @return List<Activity>
     */
    List<Activity> findByNameContainingIgnoreCase(String name);

    Page<Activity> findByEventId(Integer eventId, Pageable pageable);

    @Query("SELECT a FROM Activity a WHERE a.id IN :ids")
    Page<Activity> findByIdIn(@Param("ids") List<Integer> ids, Pageable pageable);
} 