package com.blockchain.csr.service.Impl;

import com.blockchain.csr.mapper.ActivityMapper;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangrucheng on 2025/5/20
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;


    public int deleteByPrimaryKey(Integer id) {
        return activityMapper.deleteByPrimaryKey(id);
    }


    public int insert(Activity record) {
        return activityMapper.insert(record);
    }


    public int insertSelective(Activity record) {
        return activityMapper.insertSelective(record);
    }


    public Activity selectByPrimaryKey(Integer id) {
        return activityMapper.selectByPrimaryKey(id);
    }


    public int updateByPrimaryKeySelective(Activity record) {
        return activityMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(Activity record) {
        return activityMapper.updateByPrimaryKey(record);
    }
}
