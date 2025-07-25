package com.blockchain.csr.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewerUpdateRequest {
    
    @NotNull(message = "Reviewer ID cannot be null")
    private Integer reviewerId;
} 