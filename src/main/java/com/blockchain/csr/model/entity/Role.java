package com.blockchain.csr.model.entity;

import lombok.Data;

import jakarta.persistence.*;

/**
 * @author zhangrucheng on 2025/5/19
 */

@Entity
@Data
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role", length = 45)
    private String role;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "event_name", length = 45)
    private String eventName;

    @Column(name = "activity_name", length = 45)
    private String activityName;

    // JPA relationships (optional - can be added later if needed)
    // @ManyToOne
    // @JoinColumn(name = "activity_id", insertable = false, updatable = false)
    // private Activity activity;
}