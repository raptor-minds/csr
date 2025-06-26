package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEventDto {
    private Integer id;
    private String name;
    private String type;     // 线上事件/线下事件/混合事件
    private String duration; // 8小时
    private String status;   // active/ended
} 