package com.blockchain.csr.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchDeleteRequest {
    
    @NotNull(message = "User IDs cannot be null")
    @NotEmpty(message = "User IDs cannot be empty")
    private List<Integer> userIds;
} 