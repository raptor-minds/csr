package com.blockchain.csr.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.blockchain.csr.model.entity.Event;
import com.blockchain.csr.mapper.EventMapper;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
public class EventService{

    @Autowired
    private EventMapper eventMapper;

    
    public int deleteByPrimaryKey(Integer id) {
        return eventMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Event record) {
        return eventMapper.insert(record);
    }

    
    public int insertSelective(Event record) {
        return eventMapper.insertSelective(record);
    }

    
    public Event selectByPrimaryKey(Integer id) {
        return eventMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Event record) {
        return eventMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Event record) {
        return eventMapper.updateByPrimaryKey(record);
    }

}
