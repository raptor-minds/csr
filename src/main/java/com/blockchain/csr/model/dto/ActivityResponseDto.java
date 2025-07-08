package com.blockchain.csr.model.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ActivityResponseDto {
    private Integer id;
    private String name;
    private Integer eventId;
    private Integer templateId;
    private Integer duration;
    private String icon;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
    private String status;
    private List<String> visibleLocations;
    private List<String> visibleRoles;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
    
    // New fields for enhanced response
    private Integer totalParticipants;
    private Integer totalTime;
    
    // Image fields
    private String image1;
    private String image2;
}