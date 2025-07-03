package com.blockchain.csr.model.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActivityResponseDto {
    private Integer id;
    private String name;
    private Integer eventId;
    private Integer templateId;
    private Integer duration;
    private String icon;
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private List<String> visibleLocations;
    private List<String> visibleRoles;
    private LocalDateTime createdAt;
    
    // New fields for enhanced response
    private Integer totalParticipants;
    private Integer totalTime;
}