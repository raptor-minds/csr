package com.blockchain.csr.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.blockchain.csr.model.entity.UserActivity;
import com.blockchain.csr.mapper.UserActivityMapper;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
public class UserActivityService{

    @Autowired
    private UserActivityMapper userActivityMapper;

    
    public int deleteByPrimaryKey(Integer id) {
        return userActivityMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(UserActivity record) {
        return userActivityMapper.insert(record);
    }

    
    public int insertSelective(UserActivity record) {
        return userActivityMapper.insertSelective(record);
    }

    
    public UserActivity selectByPrimaryKey(Integer id) {
        return userActivityMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(UserActivity record) {
        return userActivityMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(UserActivity record) {
        return userActivityMapper.updateByPrimaryKey(record);
    }

}
