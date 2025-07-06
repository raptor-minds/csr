package com.blockchain.csr.repository;

import com.blockchain.csr.model.entity.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Template entity
 */
@Repository
public interface TemplateRepository extends JpaRepository<Template, Integer> {
    
    /**
     * Find templates by name containing
     *
     * @param name the name
     * @return List<Template>
     */
    List<Template> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find templates by name containing with pagination
     *
     * @param name the name
     * @param pageable pagination information
     * @return Page<Template>
     */
    Page<Template> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    /**
     * Find templates by total time
     *
     * @param totalTime the total time
     * @return List<Template>
     */
    List<Template> findByTotalTime(Integer totalTime);
} 