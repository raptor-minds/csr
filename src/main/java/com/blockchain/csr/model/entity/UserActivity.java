package com.blockchain.csr.model.entity;

import com.blockchain.csr.config.JsonConverter;
import lombok.Data;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;


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
    private LocalDateTime endorsedAt;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "chain_id", length = 45)
    private String chainId;

    @Column(name = "detail", columnDefinition = "JSON")
    @Convert(converter = JsonConverter.class)
    private Serializable detail;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    // JPA relationships (optional - can be added later if needed)
    // @ManyToOne
    // @JoinColumn(name = "user_id", insertable = false, updatable = false)
    // private User user;

    // @ManyToOne
    // @JoinColumn(name = "activity_id", insertable = false, updatable = false)
    // private Activity activity;
}