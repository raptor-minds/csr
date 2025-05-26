package com.blockchain.csr.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Entity
@Data
public class UserRoleMap {
    @Id
    private Integer id;

    /**
    * 
    */
    private Integer roleId;

    /**
    * 
    */
    private Integer userId;
}