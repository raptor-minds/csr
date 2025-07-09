package com.blockchain.csr.model.dto;

import lombok.Data;

@Data
public class FeedbackDto {
    private Integer id;
    private Integer userId;
    private String feedbackDetail;
} 