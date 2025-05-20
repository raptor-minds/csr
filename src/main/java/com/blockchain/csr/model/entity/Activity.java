package com.blockchain.csr.model.entity;

/**
 * @author zhangrucheng on 2025/5/19
 */
public class Activity {
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

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}