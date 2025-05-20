package com.blockchain.csr.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.blockchain.csr.model.entity.UserRoleMap;
import com.blockchain.csr.mapper.UserRoleMapMapper;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
public class UserRoleMapService{

    @Autowired
    private UserRoleMapMapper userRoleMapMapper;

    
    public int deleteByPrimaryKey(Integer id) {
        return userRoleMapMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(UserRoleMap record) {
        return userRoleMapMapper.insert(record);
    }

    
    public int insertSelective(UserRoleMap record) {
        return userRoleMapMapper.insertSelective(record);
    }

    
    public UserRoleMap selectByPrimaryKey(Integer id) {
        return userRoleMapMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(UserRoleMap record) {
        return userRoleMapMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(UserRoleMap record) {
        return userRoleMapMapper.updateByPrimaryKey(record);
    }

}
