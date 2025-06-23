package com.blockchain.csr.model.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;


/**
 * @author zhangrucheng on 2025/5/19
 */
@Entity
@Data
@Table(name = "user_activity")
public class UserActivity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "state", length = 45)
    private String state;

    @Column(name = "endorsed_by")
    private Integer endorsedBy;

    @Column(name = "endorsed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endorsedAt;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "chain_id", length = 45)
    private String chainId;

    @Column(name = "detail", columnDefinition = "JSON")
    private String detail;

    // JPA relationships (optional - can be added later if needed)
    // @ManyToOne
    // @JoinColumn(name = "user_id", insertable = false, updatable = false)
    // private User user;

    // @ManyToOne
    // @JoinColumn(name = "activity_id", insertable = false, updatable = false)
    // private Activity activity;
}