package com.blockchain.csr.service;

import com.blockchain.csr.model.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.blockchain.csr.model.entity.UserActivity;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.repository.UserActivityRepository;
import com.blockchain.csr.repository.ActivityRepository;
import com.blockchain.csr.model.enums.UserActivityState;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Map;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserActivityService{

    private final UserActivityRepository userActivityRepository;
    private final ActivityRepository activityRepository;
    private final ActivityDetailFactory activityDetailFactory;
    private final ObjectMapper objectMapper;

    public int deleteByPrimaryKey(Integer id) {
        userActivityRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(UserActivity record) {
        userActivityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(UserActivity record) {
        userActivityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public UserActivity selectByPrimaryKey(Integer id) {
        return userActivityRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(UserActivity record) {
        userActivityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(UserActivity record) {
        userActivityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    /**
     * Update activity detail for a user
     *
     * @param request the activity detail request
     * @throws IllegalArgumentException if validation fails
     */
    public void updateActivityDetail(ActivityDetailRequest request) {
        try {
            log.info("Updating activity detail for user ID: {}, activity ID: {}", 
                    request.getUserId(), request.getActivityId());
            
            // 1. Find the user activity record
            UserActivity userActivity = userActivityRepository
                    .findByUserIdAndActivityId(request.getUserId(), request.getActivityId());
            
            if (ObjectUtils.isEmpty(userActivity)) {
                throw new IllegalArgumentException("User has not signed up for this activity");
            }
            
            // 2. Check if user is signed up (state = "SIGNED_UP")
            if (!UserActivityState.SIGNED_UP.getValue().equals(userActivity.getState())) {
                throw new IllegalArgumentException("User is not signed up for this activity");
            }
            
            // 3. Get the activity to determine template_id
            Optional<Activity> activityOpt = activityRepository.findById(request.getActivityId());
            if (activityOpt.isEmpty()) {
                throw new IllegalArgumentException("Activity not found");
            }
            
            Activity activity = activityOpt.get();
            Integer templateId = activity.getTemplateId();
            
            if (templateId == null) {
                throw new IllegalArgumentException("Activity has no template assigned");
            }
            
            // 4. Create detail using factory
            BasicDetailDTO detail = activityDetailFactory.createDetail(templateId, request.getDetail());
            
            // 5. Update the user activity record
            userActivity.setDetail(detail);
            userActivityRepository.save(userActivity);
            
            log.info("Successfully updated activity detail for user ID: {}, activity ID: {}", 
                    request.getUserId(), request.getActivityId());
            
        } catch (Exception e) {
            log.error("Error updating activity detail for user ID {}, activity ID {}: {}", 
                    request.getUserId(), request.getActivityId(), e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Get all activity details for a user
     *
     * @param userId the user ID
     * @return UserActivityDetailsResponse containing all activity details
     */
    public UserActivityDetailsResponse getUserActivityDetails(Integer userId) {
        try {
            log.info("Fetching activity details for user ID: {}", userId);
            
            // Get all user activities for the user
            List<UserActivity> userActivities = userActivityRepository.findByUserId(userId);
            
            List<ActivityDetailResponseDTO> activityDetails = userActivities.stream()
                    .filter(ua -> ua.getDetail() != null)
                    .map(this::convertToActivityDetailResponse)
                    .filter(detail -> detail != null)
                    .collect(Collectors.toList());
            
            return new UserActivityDetailsResponse(activityDetails);
            
        } catch (Exception e) {
            log.error("Error fetching activity details for user ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user activity details", e);
        }
    }
    
    /**
     * 获取指定userId和activityId的最新一条UserActivity记录
     */
    public UserActivityDto getLatestUserActivity(Integer userId, Integer activityId) {
        UserActivity userActivity = userActivityRepository.findTopByUserIdAndActivityIdOrderByCreatedAtDesc(userId, activityId);
        if (userActivity == null) {
            return null;
        }
        // 简单转换为DTO
        return UserActivityDto.builder()
                .id(userActivity.getId())
                .userId(userActivity.getUserId())
                .activityId(userActivity.getActivityId())
                .state(userActivity.getState())
                .endorsedBy(userActivity.getEndorsedBy())
                .endorsedAt(userActivity.getEndorsedAt())
                .createdAt(userActivity.getCreatedAt())
                .chainId(userActivity.getChainId())
                .detail(userActivity.getDetail() instanceof BasicDetailDTO ? (BasicDetailDTO) userActivity.getDetail() : null)
                .build();
    }
    
    /**
     * Convert UserActivity to ActivityDetailResponseDTO
     *
     * @param userActivity the user activity entity
     * @return ActivityDetailResponseDTO
     */
    private ActivityDetailResponseDTO convertToActivityDetailResponse(UserActivity userActivity) {
        try {
            // Get the activity to find template_id
            Optional<Activity> activityOpt = activityRepository.findById(userActivity.getActivityId());
            if (activityOpt.isEmpty()) {
                log.warn("Activity not found for ID: {}", userActivity.getActivityId());
                return null;
            }
            
            Activity activity = activityOpt.get();
            Integer templateId = activity.getTemplateId();
            
            BasicDetailDTO detail = activityDetailFactory.createDetail(templateId, userActivity.getDetail());
            
            // Convert to map for response
            Map<String, Object> detailMap = null;
            if (detail != null) {
                try {
                    String detailJson = objectMapper.writeValueAsString(detail);
                    detailMap = objectMapper.readValue(detailJson, Map.class);
                } catch (JsonProcessingException e) {
                    log.warn("Failed to convert detail to map for user activity {}: {}", userActivity.getId(), e.getMessage());
                }
            }
            
            return new ActivityDetailResponseDTO(
                    userActivity.getActivityId(),
                    templateId,
                    detailMap
            );
            
        } catch (Exception e) {
            log.error("Error converting user activity to response DTO: {}", e.getMessage(), e);
            return null;
        }
    }
}
