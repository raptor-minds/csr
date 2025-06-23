package com.blockchain.csr.model.entity;

import lombok.Data;

import jakarta.persistence.*;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Entity
@Data
@Table(name = "template")
public class Template {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "total_time")
    private Integer totalTime;

    @Column(name = "file_link", length = 45)
    private String fileLink;

    @Column(name = "detail", columnDefinition = "JSON")
    private String detail;
}