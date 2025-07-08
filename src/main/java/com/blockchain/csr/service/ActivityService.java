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
import java.time.LocalDateTime;
// 添加分页相关import
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.Map;
import java.util.stream.Collectors;
import com.blockchain.csr.model.dto.UserActivityDto;

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
    public Integer createActivity(Activity record) {
        // Set created_at to current system time
        record.setCreatedAt(LocalDateTime.now());
        Activity savedActivity = activityRepository.save(record);
        return savedActivity.getId(); // 返回实际创建的活动ID
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

    /**
     * Get total participants count for an activity (only SIGNED_UP users)
     *
     * @param activityId the activity ID
     * @return number of signed up participants
     */
    public Integer getTotalParticipants(Integer activityId) {
        Integer count = userActivityRepository.countSignedUpParticipantsByActivityId(activityId);
        return count != null ? count : 0;
    }

    /**
     * Calculate total time for an activity (participants * duration)
     *
     * @param activity the activity
     * @param totalParticipants the number of participants
     * @return total time (participants * duration)
     */
    public Integer calculateTotalTime(Activity activity, Integer totalParticipants) {
        if (activity.getDuration() == null || totalParticipants == null) {
            return 0;
        }
        return totalParticipants * activity.getDuration();
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

    /**
     * 获取用户在某事件下参与的所有活动
     */
    public List<UserActivityDto> getUserActivitiesByEvent(Integer userId, Integer eventId, Integer page, Integer pageSize) {
        List<UserActivity> userActivities = userActivityRepository.findByUserIdAndEventId(userId, eventId);
        List<Integer> activityIds = userActivities.stream()
                .filter(ua -> ua.getState() != null && ua.getState().equals("SIGNED_UP"))
                .map(UserActivity::getActivityId)
                .toList();
        if (activityIds.isEmpty()) return List.of();

        Pageable pageable = Pageable.ofSize(pageSize != null ? pageSize : 10).withPage(page != null ? page - 1 : 0);
        Page<Activity> activityPage = activityRepository.findByIdIn(activityIds, pageable);
        List<Activity> pagedActivities = activityPage.getContent();

        // 以 activityId 为 key，便于快速查找
        Map<Integer, UserActivity> uaMap = userActivities.stream()
                .collect(Collectors.toMap(UserActivity::getActivityId, ua -> ua, (a, b) -> a));

        // 组装DTO
        return pagedActivities.stream()
                .map(act -> {
                    UserActivity ua = uaMap.get(act.getId());
                    if (ua == null) return null;
                    UserActivityDto dto = new UserActivityDto();
                    dto.setId(ua.getId());
                    dto.setUserId(ua.getUserId());
                    dto.setActivityId(ua.getActivityId());
                    dto.setState(ua.getState());
                    dto.setEndorsedBy(ua.getEndorsedBy());
                    dto.setEndorsedAt(ua.getEndorsedAt());
                    dto.setCreatedAt(ua.getCreatedAt());
                    dto.setChainId(ua.getChainId());
                    dto.setDetail(ua.getDetail());
                    // 业务字段
                    dto.setName(act.getName());
                    dto.setDuration(act.getDuration());
                    // eventName 可选：如需可补充
                    return dto;
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }

//    /**
//     * 获取某用户已报名的所有活动
//     */
//    public List<Activity> getUserActivities(Integer userId) {
//        List<UserActivity> userActivities = userActivityRepository.findByUserId(userId);
//        // 只返回用户已报名的活动
//        List<Integer> activityIds = userActivities.stream()
//                .filter(ua -> ua.getState() != null && ua.getState().equals("SIGNED_UP"))
//                .map(UserActivity::getActivityId)
//                .toList();
//        if (activityIds.isEmpty()) return List.of();
//        return activityRepository.findAllById(activityIds);
//    }
}