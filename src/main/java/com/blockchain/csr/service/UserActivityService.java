package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.blockchain.csr.model.entity.UserActivity;
import com.blockchain.csr.repository.UserActivityRepository;
import com.blockchain.csr.model.dto.UserActivityDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserActivityService{

    private final UserActivityRepository userActivityRepository;

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
        Integer totalTime = (Integer) result[3];
        
        String duration = totalTime != null ? totalTime + "分钟" : "未知";
        
        return UserActivityDto.builder()
                .id(activityId)
                .name(activityName)
                .eventName(eventName)
                .duration(duration)
                .build();
    }
}
