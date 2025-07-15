package com.blockchain.csr.service;

import com.blockchain.csr.model.dto.BasicDetailDTO;
import com.blockchain.csr.model.mapper.ActivityMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.model.entity.UserActivity;
import com.blockchain.csr.model.enums.UserActivityState;
import com.blockchain.csr.repository.ActivityRepository;
import com.blockchain.csr.repository.UserActivityRepository;
import com.blockchain.csr.repository.UserRepository;

import java.time.ZoneId;
import java.util.List;
import java.util.Date;
import java.time.LocalDateTime;
// 添加分页相关import
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.Map;
import java.util.stream.Collectors;
import com.blockchain.csr.model.dto.UserActivityDto;
import com.blockchain.csr.model.dto.ActivityResponseDto;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ActivityService{

    private final ActivityRepository activityRepository;
    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;
    private final ActivityMapper activityMapper;
    private final ObjectMapper objectMapper;
    private final ActivityDetailFactory activityDetailFactory;

    // 获取活动详情
    public Activity getActivityById(Integer id) {
        return activityRepository.findById(id).orElse(null);
    }

    // 创建活动
    public Integer createActivity(Activity record) {
        // Set created_at to current system time
        record.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
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
        if (existingRecords.size() > 1) {
            log.warn("Multiple active user_activity for user ID: {}", userId);
        }
        if (!ObjectUtils.isEmpty(existingRecords)) {
            UserActivity existingRecord = existingRecords.get(0);
            // 如果已经是SIGNED_UP状态且未被删除，不允许重复报名
            if (UserActivityState.SIGNED_UP.getValue().equals(existingRecord.getState()) && 
                (existingRecord.getDeleted() == null || !existingRecord.getDeleted())) {
                throw new IllegalArgumentException("User has already signed up for this activity");
            }
            // 如果是WITHDRAWN状态或者被删除，允许重新报名
            else if (UserActivityState.WITHDRAWN.getValue().equals(existingRecord.getState()) || 
                     (existingRecord.getDeleted() != null && existingRecord.getDeleted())) {
                existingRecord.setState(UserActivityState.SIGNED_UP.getValue());
                existingRecord.setDeleted(false); // 重新激活记录
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
        
        try {
            userActivityRepository.save(userActivity);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Handle unique constraint violation
            if (e.getMessage().contains("uk_user_activity") || 
                e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("User has already signed up for this activity");
            }
            throw e; // Re-throw if it's a different constraint violation
        }
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

        
        if (ObjectUtils.isEmpty(existingRecords)) {
            throw new IllegalArgumentException("User has not signed up for this activity");
        }

        existingRecords.stream().forEach(existingRecord -> {
            existingRecord.setState(UserActivityState.WITHDRAWN.getValue());
            existingRecord.setDeleted(true);
        });
        userActivityRepository.saveAll(existingRecords);
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
                    
                    // Convert detail with null checking
                    BasicDetailDTO detailDto = null;
                    if (ua.getDetail() != null && act.getTemplateId() != null) {
                        detailDto = activityDetailFactory.createDetail(act.getTemplateId(), ua.getDetail());
                    }
                    dto.setDetail(detailDto);
                    
                    // 业务字段
                    dto.setName(act.getName());
                    dto.setDuration(act.getDuration());
                    // eventName 可选：如需可补充
                    return dto;
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Get activities with optional user filtering and enhanced response
     * 
     * @param eventId Filter by event ID
     * @param userId If provided, get activities user has signed up for
     * @param page Page number
     * @param pageSize Page size
     * @return List of activities with optional user activity details
     */
    public List<ActivityResponseDto> getActivitiesWithUserDetails(Integer eventId, Integer userId, 
                                                                  Integer page, Integer pageSize) {
        List<Activity> activities;
        Map<Integer, UserActivity> userActivityMap = new HashMap<>();
        
        // Step 1: Get activities based on userId presence
        if (userId != null) {
            // Get all activities the user has signed up for
            List<UserActivity> userActivities = userActivityRepository.findByUserId(userId);
            
            // Filter to only SIGNED_UP activities that are not deleted
            List<UserActivity> signedUpActivities = userActivities.stream()
                    .filter(ua -> "SIGNED_UP".equals(ua.getState()) && 
                                  (ua.getDeleted() == null || !ua.getDeleted()))
                    .collect(Collectors.toList());
            
            // Create map for quick lookup
            userActivityMap = signedUpActivities.stream()
                    .collect(Collectors.toMap(UserActivity::getActivityId, ua -> ua));
            
            // Get activity IDs
            List<Integer> activityIds = signedUpActivities.stream()
                    .map(UserActivity::getActivityId)
                    .collect(Collectors.toList());
            
            if (activityIds.isEmpty()) {
                return new ArrayList<>();
            }
            
            // Get activities by IDs
            activities = activityRepository.findAllById(activityIds);
        } else {
            // Get all activities
            activities = activityRepository.findAll();
        }
        
        // Step 2: Filter by eventId if provided
        if (eventId != null) {
            activities = activities.stream()
                    .filter(activity -> eventId.equals(activity.getEventId()))
                    .collect(Collectors.toList());
        }
        
        // Step 3: Apply pagination
        if (page != null && pageSize != null) {
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, activities.size());
            if (startIndex < activities.size()) {
                activities = activities.subList(startIndex, endIndex);
            } else {
                activities = new ArrayList<>();
            }
        }
        
        // Step 4: Convert to DTOs with enhanced information
        final Map<Integer, UserActivity> finalUserActivityMap = userActivityMap;
        return activities.stream()
                .map(activity -> {
                    ActivityResponseDto dto = activityMapper.toResponseDto(activity);

                    Integer totalParticipants = getTotalParticipants(activity.getId());
                    Integer totalTime = calculateTotalTime(activity, totalParticipants);
                    dto.setTotalParticipants(totalParticipants);
                    dto.setTotalTime(totalTime);
                    
                    // User activity details if userId was provided
                    if (userId != null) {
                        UserActivity userActivity = finalUserActivityMap.get(activity.getId());
                        if (userActivity != null) {
                            dto.setUserActivityState(userActivity.getState());
                            dto.setUserActivityCreatedAt(userActivity.getCreatedAt());
                            dto.setUserActivityChainId(userActivity.getChainId());
                            
                            // Convert user activity detail
                            BasicDetailDTO userDetail = null;
                            if (userActivity.getDetail() != null) {
                                userDetail = activityDetailFactory.createDetail(activity.getTemplateId(), userActivity.getDetail());
                            }
                            dto.setUserActivityDetail(userDetail);
                        }
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Helper method to parse JSON string to List<String>
     */
    private List<String> parseJsonToList(String jsonString) {
        try {
            // Simple JSON array parsing - you might want to use a proper JSON library
            if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
                String content = jsonString.substring(1, jsonString.length() - 1);
                if (content.trim().isEmpty()) {
                    return new ArrayList<>();
                }
                return java.util.Arrays.stream(content.split(","))
                        .map(s -> s.trim().replaceAll("\"", ""))
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
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