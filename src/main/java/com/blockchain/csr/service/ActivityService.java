package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.model.entity.UserActivity;
import com.blockchain.csr.model.enums.UserActivityState;
import com.blockchain.csr.repository.ActivityRepository;
import com.blockchain.csr.repository.UserActivityRepository;
import com.blockchain.csr.repository.UserRepository;
import java.util.List;
import java.util.Date;
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
    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;

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

    // 用户报名活动
    public void signupActivity(Integer activityId, Integer userId) {
        // 验证活动是否存在
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
        
        // 验证用户是否存在
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // 检查用户是否已经有该活动的记录
        List<UserActivity> existingRecords = userActivityRepository.findByUserIdAndActivityId(userId, activityId);
        
        if (!existingRecords.isEmpty()) {
            UserActivity existingRecord = existingRecords.get(0);
            // 如果已经是SIGNED_UP状态，不允许重复报名
            if (UserActivityState.SIGNED_UP.getValue().equals(existingRecord.getState())) {
                throw new IllegalArgumentException("User has already signed up for this activity");
            }
            // 如果是WITHDRAWN状态，允许重新报名，更新状态为SIGNED_UP
            else if (UserActivityState.WITHDRAWN.getValue().equals(existingRecord.getState())) {
                existingRecord.setState(UserActivityState.SIGNED_UP.getValue());
                existingRecord.setCreatedAt(new Date()); // 更新报名时间
                userActivityRepository.save(existingRecord);
                return;
            }
        }
        
        // 创建新的报名记录
        UserActivity userActivity = new UserActivity();
        userActivity.setUserId(userId);
        userActivity.setActivityId(activityId);
        userActivity.setState(UserActivityState.SIGNED_UP.getValue());
        userActivity.setCreatedAt(new Date());
        // chain_id, detail, endorsed_at, endorsed_by 字段设为空，符合要求
        
        userActivityRepository.save(userActivity);
    }

    // 用户退出活动
    public void withdrawActivity(Integer activityId, Integer userId) {
        // 验证活动是否存在
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
        
        // 验证用户是否存在
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // 查找用户的活动记录
        List<UserActivity> existingRecords = userActivityRepository.findByUserIdAndActivityId(userId, activityId);
        
        if (existingRecords.isEmpty()) {
            throw new IllegalArgumentException("User has not signed up for this activity");
        }
        
        UserActivity existingRecord = existingRecords.get(0);
        
        // 只有SIGNED_UP状态才能退出
        if (!UserActivityState.SIGNED_UP.getValue().equals(existingRecord.getState())) {
            throw new IllegalArgumentException("User is not currently signed up for this activity");
        }
        
        // 更新状态为WITHDRAWN
        existingRecord.setState(UserActivityState.WITHDRAWN.getValue());
        userActivityRepository.save(existingRecord);
    }
}