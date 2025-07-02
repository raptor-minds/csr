package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    private Integer id;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String status;
    private String createdAt;
    
    // Enhanced fields for when needsTotal=true
    private Integer totalParticipants;
    private Integer totalTime;
} 