package com.blockchain.csr.model.entity;

import lombok.Data;

import jakarta.persistence.*;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Entity
@Data
@Table(name = "user_event")
public class UserEvent {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "event_id")
    private Integer eventId;

    // JPA relationships (optional - can be added later if needed)
    // @ManyToOne
    // @JoinColumn(name = "user_id", insertable = false, updatable = false)
    // private User user;

    // @ManyToOne
    // @JoinColumn(name = "event_id", insertable = false, updatable = false)
    // private Event event;
}