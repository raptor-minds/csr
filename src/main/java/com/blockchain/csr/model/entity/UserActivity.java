package com.blockchain.csr.model.entity;

import lombok.Data;

import java.util.Date;


/**
 * @author zhangrucheng on 2025/5/19
 */
@Data
public class UserActivity {
    /**
    * 
    */
    private Integer id;

    /**
    * 
    */
    private Integer userId;

    /**
    * 
    */
    private Integer activityId;

    /**
    * 
    */
    private String state;

    /**
    * 
    */
    private Integer endorsedBy;

    /**
    * 
    */
    private Date endorsedAt;

    /**
    * 
    */
    private Date createdAt;

    /**
    * 
    */
    private String chainId;

    /**
    * 
    */
    private String detail;
}