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
public class EventListResponse {
    private List<EventWithActivitiesDto> data;
    private Long total;
    private Integer page;
    private Integer pageSize;
} 