package com.blockchain.csr.model.entity;

import com.blockchain.csr.model.enums.EventStatus;
import com.blockchain.csr.model.enums.EventType;
import lombok.Data;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @Column(name = "name", length = 100, nullable = false)
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
    private LocalDateTime startTime;

    /**
    * 
    */
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

    /**
    * 
    */
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

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
    @Column(name = "avatar", length = 2000)
    private String avatar;

    /**
    * 
    */
    @Column(name = "vendor_id", length = 45)
    private String vendorId;

    @Column(name = "type", length = 20, nullable = false)
    private String type = EventType.HYBRID.getValue();

    @Column(name = "status", length = 20, nullable = false)
    private String status = EventStatus.ACTIVE.getValue();

    /**
    * 可见地区，JSON字符串
    */
    @Column(name = "visible_locations", length = 1000)
    private String visibleLocations;

    /**
    * 可见角色，JSON字符串
    */
    @Column(name = "visible_roles", length = 1000)
    private String visibleRoles;

    /**
    * 是否展示
    */
    @Column(name = "is_display")
    private Boolean isDisplay;

    /**
    * 详情图片
    */
    @Column(name = "detail_image", length = 2000)
    private String detailImage;
}