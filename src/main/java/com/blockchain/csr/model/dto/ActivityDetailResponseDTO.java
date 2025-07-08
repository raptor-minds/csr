package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Response DTO for activity detail information
 * Used for GET /api/users/{userId}/activity-details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDetailResponseDTO {
    
    private Integer activityId;
    private Integer templateId;
    private Map<String, Object> details;
} 