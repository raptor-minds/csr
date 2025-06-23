package com.blockchain.csr.model.entity;

import lombok.Data;

import jakarta.persistence.*;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Entity
@Data
@Table(name = "user_role_map")
public class UserRoleMap {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "user_id")
    private Integer userId;

    // JPA relationships (optional - can be added later if needed)
    // @ManyToOne
    // @JoinColumn(name = "role_id", insertable = false, updatable = false)
    // private Role role;

    // @ManyToOne
    // @JoinColumn(name = "user_id", insertable = false, updatable = false)
    // private User user;
}