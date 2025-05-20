package com.blockchain.csr.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.mapper.ActivityMapper;

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
