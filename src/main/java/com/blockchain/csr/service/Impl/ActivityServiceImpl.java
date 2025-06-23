package com.blockchain.csr.service.Impl;

import com.blockchain.csr.repository.ActivityRepository;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhangrucheng on 2025/5/20
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;


    public int deleteByPrimaryKey(Integer id) {
        activityRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(Activity record) {
        activityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(Activity record) {
        activityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public Activity selectByPrimaryKey(Integer id) {
        return activityRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(Activity record) {
        activityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(Activity record) {
        activityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }
}
