package com.blockchain.csr.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * The type User.
 *
 * @author zhangrucheng on 2025/5/18
 */
@Getter
@Setter
public class User {
    /**
    * 
    */
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