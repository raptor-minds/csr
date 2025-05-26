package com.blockchain.csr.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author zhangrucheng on 2025/5/19
 */

@Entity
@Data
public class Role {
    @Id
    private Integer id;

    /**
    * 
    */
    private String role;

    /**
    * 
    */
    private Integer activityId;

    /**
    * 
    */
    private String eventName;

    /**
    * 
    */
    private String activityName;
}