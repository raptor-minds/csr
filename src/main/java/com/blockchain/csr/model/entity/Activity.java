package com.blockchain.csr.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Entity
@Data
public class Activity {
    @Id
    private Integer id;

    /**
    * 
    */
    private String name;

    /**
    * 
    */
    private Integer eventId;

    /**
    * 
    */
    private Integer templateId;

    /**
    * 
    */
    private Integer totalTime;

    /**
    * 
    */
    private String icon;

    /**
    * 
    */
    private String description;
}