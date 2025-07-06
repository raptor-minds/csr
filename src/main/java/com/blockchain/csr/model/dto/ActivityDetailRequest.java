package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * Request DTO for updating activity details
 * Used for POST /api/user/activity-detail
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDetailRequest {
    
    @NotNull(message = "User ID is required")
    private Integer userId;
    
    @NotNull(message = "Activity ID is required")
    private Integer activityId;

    private Map<String, Object> detail;
} 