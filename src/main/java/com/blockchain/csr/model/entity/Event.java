package com.blockchain.csr.model.entity;

import java.util.Date;

/**
 * @author zhangrucheng on 2025/5/19
 */
public class Event {
    /**
    * 
    */
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }
}