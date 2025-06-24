package com.blockchain.csr.model.entity;

import com.blockchain.csr.model.enums.UserRole;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * The type User.
 *
 * @author zhangrucheng on 2025/5/18
 */
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", length = 45, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 10, nullable = false)
    private UserRole role = UserRole.USER; // Default role is USER

    @Column(name = "location", length = 50)
    private String location;

    @Column(name = "reviewer", length = 100)
    private String reviewer;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
}