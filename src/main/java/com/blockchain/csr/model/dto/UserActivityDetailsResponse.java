package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Response wrapper for user activity details
 * Used for GET /api/users/{userId}/activity-details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityDetailsResponse {
    
    private List<ActivityDetailResponseDTO> activityDetails;
} 