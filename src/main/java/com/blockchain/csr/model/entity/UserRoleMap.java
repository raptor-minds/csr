package com.blockchain.csr.model.entity;

/**
 * @author zhangrucheng on 2025/5/19
 */
public class UserRoleMap {
    /**
    * 
    */
    private Integer id;

    /**
    * 
    */
    private Integer roleId;

    /**
    * 
    */
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}