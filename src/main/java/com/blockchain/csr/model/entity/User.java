package com.blockchain.csr.model.entity;

import lombok.Data;

import jakarta.persistence.*;

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
}