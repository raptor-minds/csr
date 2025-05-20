package com.blockchain.csr.model.entity;

import java.util.Date;

/**
 * @author zhangrucheng on 2025/5/19
 */
public class Attachment {
    /**
    * 
    */
    private Integer id;

    /**
    * 
    */
    private Integer userActivityId;

    /**
    * 
    */
    private String name;

    /**
    * 
    */
    private String url;

    /**
    * 
    */
    private Date createAt;

    /**
    * 
    */
    private String chainId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserActivityId() {
        return userActivityId;
    }

    public void setUserActivityId(Integer userActivityId) {
        this.userActivityId = userActivityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }
}