package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
} 