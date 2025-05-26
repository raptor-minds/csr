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
public class Attachment {
    @Id
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
}