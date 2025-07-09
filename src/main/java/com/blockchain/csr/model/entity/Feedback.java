package com.blockchain.csr.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    @Column(length = 1000)
    private String feedbackDetail;
} 