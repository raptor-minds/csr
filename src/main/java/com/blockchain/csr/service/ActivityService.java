package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.repository.ActivityRepository;
import java.util.List;
// 添加分页相关import
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService{

    private final ActivityRepository activityRepository;

    // 获取活动详情
    public Activity getActivityById(Integer id) {
        return activityRepository.findById(id).orElse(null);
    }

    // 创建活动
    public int createActivity(Activity record) {
        activityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    // 更新活动
    public void updateActivity(Activity record) {
        activityRepository.save(record);
    }

    // 删除活动
    public void deleteActivity(Integer id) {
        activityRepository.deleteById(id);
    }

    // 根据事件ID获取活动列表
    public List<Activity> getActivities(Integer eventId, Integer page, Integer pageSize) {
        if (eventId == null) {
            return activityRepository.findAll().stream().toList();
        } else {
            Pageable pageable = Pageable.ofSize(pageSize != null ? pageSize : 10).withPage(page != null ? page - 1 : 0);
            Page<Activity> activityPage = activityRepository.findByEventId(eventId, pageable);
            return activityPage.getContent();
        }
    }
}