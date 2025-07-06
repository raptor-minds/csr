package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * Basic detail DTO for activity details
 * Used for template_id = 1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicDetailDTO {
    
    @NotBlank(message = "Comment is required")
    private String comment;
} 