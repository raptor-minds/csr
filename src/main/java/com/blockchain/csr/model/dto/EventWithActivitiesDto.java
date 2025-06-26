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
public class EventWithActivitiesDto {
    private Integer id;
    private String name;
    private String startTime;
    private String endTime;
    private Boolean isDisplay;
    private String bgImage;
    private List<ActivityDto> activities;
} 