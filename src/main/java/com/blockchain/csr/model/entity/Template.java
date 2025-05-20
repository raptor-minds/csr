package com.blockchain.csr.model.entity;

/**
 * @author zhangrucheng on 2025/5/19
 */
public class Template {
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
    private Integer totalTime;

    /**
    * 
    */
    private String fileLink;

    /**
    * 
    */
    private String detail;

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

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}