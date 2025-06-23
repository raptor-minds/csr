package com.blockchain.csr.repository;

import com.blockchain.csr.model.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Attachment entity
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    
    /**
     * Find attachments by user activity ID
     *
     * @param userActivityId the user activity ID
     * @return List<Attachment>
     */
    List<Attachment> findByUserActivityId(Integer userActivityId);
    
    /**
     * Find attachments by name containing
     *
     * @param name the name
     * @return List<Attachment>
     */
    List<Attachment> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find attachments by chain ID
     *
     * @param chainId the chain ID
     * @return List<Attachment>
     */
    List<Attachment> findByChainId(String chainId);
} 