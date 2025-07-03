package com.blockchain.csr.repository;

import com.blockchain.csr.model.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Event entity
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    
    /**
     * Find events by name containing
     *
     * @param name the name
     * @return List<Event>
     */
    List<Event> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find events by name containing with pagination
     *
     * @param name the name
     * @param pageable the pageable
     * @return Page<Event>
     */
    Page<Event> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    /**
     * Find events by location
     *
     * @param location the location
     * @return List<Event>
     */
    List<Event> findByLocation(String location);
} 