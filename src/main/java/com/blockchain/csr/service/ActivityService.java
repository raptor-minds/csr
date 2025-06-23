package com.blockchain.csr.service;

import com.blockchain.csr.model.entity.Activity;

/**
 * @author zhangrucheng on 2025/5/19
 */

public interface ActivityService {

    int deleteByPrimaryKey(Integer id);


    int insert(Activity record);


    int insertSelective(Activity record);


    Activity selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(Activity record);


    int updateByPrimaryKey(Activity record);

}
