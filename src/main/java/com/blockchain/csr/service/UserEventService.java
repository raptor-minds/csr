package com.blockchain.csr.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.blockchain.csr.mapper.UserEventMapper;
import com.blockchain.csr.model.entity.UserEvent;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
public class UserEventService{

    @Autowired
    private UserEventMapper userEventMapper;

    
    public int deleteByPrimaryKey(Integer id) {
        return userEventMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(UserEvent record) {
        return userEventMapper.insert(record);
    }

    
    public int insertSelective(UserEvent record) {
        return userEventMapper.insertSelective(record);
    }

    
    public UserEvent selectByPrimaryKey(Integer id) {
        return userEventMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(UserEvent record) {
        return userEventMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(UserEvent record) {
        return userEventMapper.updateByPrimaryKey(record);
    }

}
