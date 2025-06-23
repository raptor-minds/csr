package com.blockchain.csr.model.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;

/**
 * @author zhangrucheng on 2025/5/19
 */

@Entity
@Data
@Table(name = "attachment")
public class Attachment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_activity_id")
    private Integer userActivityId;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "url", length = 45)
    private String url;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "chain_id", length = 45)
    private String chainId;

    // JPA relationships (optional - can be added later if needed)
    // @ManyToOne
    // @JoinColumn(name = "user_activity_id", insertable = false, updatable = false)
    // private UserActivity userActivity;
}