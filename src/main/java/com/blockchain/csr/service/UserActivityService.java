package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.blockchain.csr.model.entity.UserActivity;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.repository.UserActivityRepository;
import com.blockchain.csr.repository.ActivityRepository;
import com.blockchain.csr.model.dto.UserActivityDto;
import com.blockchain.csr.model.dto.ActivityDetailRequest;
import com.blockchain.csr.model.dto.BasicDetailDTO;
import com.blockchain.csr.model.dto.DonationDetailDTO;
import com.blockchain.csr.model.dto.ActivityDetailResponseDTO;
import com.blockchain.csr.model.dto.UserActivityDetailsResponse;
import com.blockchain.csr.model.enums.UserActivityState;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.math.BigDecimal;
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
     * Get user activities by user ID
     *
     * @param userId the user ID
     * @return List of user activity DTOs
     */
    public List<UserActivityDto> getUserActivities(Integer userId) {
        try {
            log.info("Fetching activities for user ID: {}", userId);
            
            List<Object[]> results = userActivityRepository.findUserActivitiesWithEventInfo(userId);
            
            return results.stream()
                    .map(this::convertToUserActivityDto)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            log.error("Error fetching activities for user ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user activities", e);
        }
    }
    
    /**
     * Convert database result to UserActivityDto
     *
     * @param result the database result array
     * @return UserActivityDto
     */
    private UserActivityDto convertToUserActivityDto(Object[] result) {
        Integer activityId = (Integer) result[0];
        String activityName = (String) result[1];
        String eventName = (String) result[2];
        Integer duration = (Integer) result[3];
        String state = (String) result[4];
        
        return UserActivityDto.builder()
                .id(activityId)
                .name(activityName)
                .eventName(eventName)
                .duration(duration)
                .state(state)
                .build();
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
            List<UserActivity> userActivities = userActivityRepository
                    .findByUserIdAndActivityId(request.getUserId(), request.getActivityId());
            
            if (userActivities.isEmpty()) {
                throw new IllegalArgumentException("User has not signed up for this activity");
            }
            
            UserActivity userActivity = userActivities.get(0);
            
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
            
            // 4. Validate and convert detail based on template_id
            String detailJson = validateAndConvertDetail(request.getDetail(), templateId);
            
            // 5. Update the user activity record
            userActivity.setDetail(detailJson);
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
     * Validate and convert detail based on template_id
     *
     * @param detail the detail map from request
     * @param templateId the template ID
     * @return JSON string representation of the detail
     * @throws IllegalArgumentException if validation fails
     */
    private String validateAndConvertDetail(java.util.Map<String, Object> detail, Integer templateId) {
        try {
            if (templateId == 1) {
                // BasicDetailDTO - only comment required
                String comment = (String) detail.get("comment");
                if (comment == null || comment.trim().isEmpty()) {
                    throw new IllegalArgumentException("Comment is required");
                }
                
                BasicDetailDTO basicDetail = new BasicDetailDTO(comment);
                return objectMapper.writeValueAsString(basicDetail);
                
            } else if (templateId == 2) {
                // DonationDetailDTO - comment and amount required
                String comment = (String) detail.get("comment");
                if (comment == null || comment.trim().isEmpty()) {
                    throw new IllegalArgumentException("Comment is required");
                }
                
                Object amountObj = detail.get("amount");
                if (amountObj == null) {
                    throw new IllegalArgumentException("Amount is required");
                }
                
                BigDecimal amount;
                if (amountObj instanceof String) {
                    amount = new BigDecimal((String) amountObj);
                } else if (amountObj instanceof Number) {
                    amount = BigDecimal.valueOf(((Number) amountObj).doubleValue());
                } else {
                    throw new IllegalArgumentException("Invalid amount format");
                }
                
                if (amount.compareTo(BigDecimal.valueOf(0.01)) < 0) {
                    throw new IllegalArgumentException("Amount must be greater than 0");
                }
                
                DonationDetailDTO donationDetail = new DonationDetailDTO(comment, amount);
                return objectMapper.writeValueAsString(donationDetail);
                
            } else {
                throw new IllegalArgumentException("Unsupported template ID: " + templateId);
            }
            
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to convert detail to JSON: " + e.getMessage());
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
                    .filter(ua -> ua.getDetail() != null && !ua.getDetail().trim().isEmpty())
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
            
            // Parse the detail JSON
            Map<String, Object> detailMap = null;
            if (userActivity.getDetail() != null && !userActivity.getDetail().trim().isEmpty()) {
                try {
                    detailMap = objectMapper.readValue(userActivity.getDetail(), Map.class);
                } catch (JsonProcessingException e) {
                    log.warn("Failed to parse detail JSON for user activity {}: {}", userActivity.getId(), e.getMessage());
                    detailMap = new java.util.HashMap<>();
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
