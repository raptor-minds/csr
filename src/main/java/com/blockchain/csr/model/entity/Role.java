package com.blockchain.csr.model.entity;

/**
 * @author zhangrucheng on 2025/5/19
 */
public class Role {
    /**
    * 
    */
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}