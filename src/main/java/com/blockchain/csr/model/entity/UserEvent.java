package com.blockchain.csr.model.entity;

/**
 * @author zhangrucheng on 2025/5/19
 */
public class UserEvent {
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
    private Integer eventId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}