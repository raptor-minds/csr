package com.blockchain.csr.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The type User.
 *
 * @author zhangrucheng on 2025/5/18
 */
@Data
@Entity
public class User {

    @Id
    private Integer id;

    /**
    * 
    */
    private String username;

    /**
    * 
    */
    private String password;
}