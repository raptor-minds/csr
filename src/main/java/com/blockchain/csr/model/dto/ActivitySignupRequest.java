package com.blockchain.csr.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Activity signup request DTO
 * 
 * @author system
 */
@Data
public class ActivitySignupRequest {
    
    @NotNull(message = "User ID is required")
    private Integer userId;
} 