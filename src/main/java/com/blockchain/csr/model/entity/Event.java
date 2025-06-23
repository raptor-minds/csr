package com.blockchain.csr.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Entity
@Data
public class Event {
    @Id
    private Integer id;

    /**
    * 
    */
    private String name;

    /**
    * 
    */
    private String description;

    /**
    * 
    */
    private Date startTime;

    /**
    * 
    */
    private Date endTime;

    /**
    * 
    */
    private Date createdAt;

    /**
    * 
    */
    private Integer totalTime;

    /**
    * 
    */
    private Long totalMoney;

    /**
    * 
    */
    private String location;

    /**
    * 
    */
    private String avatar;

    /**
    * 
    */
    private String vendorId;
}