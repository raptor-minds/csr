package com.blockchain.csr.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Entity
@Data
public class UserEvent {
    @Id
    private Integer id;

    /**
    * 
    */
    private Integer userId;

    /**
    * 
    */
    private Integer eventId;
}