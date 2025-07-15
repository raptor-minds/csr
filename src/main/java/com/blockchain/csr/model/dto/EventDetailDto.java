package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailDto {
    private Integer id;
    private String name;
    private String startTime;
    private String endTime;
    private String status;
    private String icon;
    private String description;
    private Boolean isDisplay;
    private List<String> visibleLocations;
    private List<String> visibleRoles;
    private String createdAt;
    private String detailImage;
} 