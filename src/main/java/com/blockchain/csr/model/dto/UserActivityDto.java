package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user activity information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityDto {
    
    /**
     * Activity ID
     */
    private Integer id;
    
    /**
     * User ID
     */
    private Integer userId;
    
    /**
     * Activity ID
     */
    private Integer activityId;
    
    /**
     * Activity name
     */
    private String name;
    
    /**
     * Event name
     */
    private String eventName;
    
    /**
     * Activity duration
     */
    private Integer duration;
    
    /**
     * User activity state (SIGNED_UP, WITHDRAWN)
     */
    private String state;
    
    /**
     * Endorsed by
     */
    private Integer endorsedBy;
    
    /**
     * Endorsed at
     */
    private LocalDateTime endorsedAt;
    
    /**
     * Created at
     */
    private LocalDateTime createdAt;
    
    /**
     * Chain ID
     */
    private String chainId;
    
    /**
     * Activity detail (polymorphic based on template)
     */
    private BasicDetailDTO detail;
} 