package com.blockchain.csr.model.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Entity
@Data
@Table(name = "event")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
    * 
    */
    @Column(name = "name", length = 45)
    private String name;

    /**
    * 
    */
    @Column(name = "description", length = 1000)
    private String description;

    /**
    * 
    */
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    /**
    * 
    */
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    /**
    * 
    */
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
    * 
    */
    @Column(name = "total_time")
    private Integer totalTime;

    /**
    * 
    */
    @Column(name = "total_money")
    private BigDecimal totalMoney;

    /**
    * 
    */
    @Column(name = "location", length = 45)
    private String location;

    /**
    * 
    */
    @Column(name = "avatar", length = 255)
    private String avatar;

    /**
    * 
    */
    @Column(name = "vendor_id", length = 45)
    private String vendorId;

    /**
    * 
    */
    @Column(name = "type", length = 20)
    private String type; // 线上事件/线下事件/混合事件

    /**
    * 事件状态 active=进行中, ended=已结束
    */
    @Column(name = "status", length = 20)
    private String status;
}