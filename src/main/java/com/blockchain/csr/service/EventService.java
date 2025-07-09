package com.blockchain.csr.service;

import com.blockchain.csr.model.dto.BasicDetailDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.blockchain.csr.model.entity.Event;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.model.entity.UserActivity;
import com.blockchain.csr.model.dto.DonationDetailDTO;
import com.blockchain.csr.repository.EventRepository;
import com.blockchain.csr.repository.ActivityRepository;
import com.blockchain.csr.repository.UserActivityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;
import java.math.BigDecimal;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventService{

    private final EventRepository eventRepository;
    private final ActivityRepository activityRepository;
    private final UserActivityRepository userActivityRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ActivityDetailFactory activityDetailFactory;

    
    public int deleteByPrimaryKey(Integer id) {
        eventRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(Event record) {
        // Set created_at to current system time
        record.setCreatedAt(new Date());
        eventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(Event record) {
        // Set created_at to current system time
        record.setCreatedAt(new Date());
        eventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public Event selectByPrimaryKey(Integer id) {
        return eventRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(Event record) {
        eventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(Event record) {
        eventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    /**
     * Get total unique participants count for an event (de-duplicated across all activities)
     *
     * @param eventId the event ID
     * @return number of unique signed up participants
     */
    public Integer getTotalParticipants(Integer eventId) {
        Integer count = userActivityRepository.countUniqueSignedUpParticipantsByEventId(eventId);
        return count != null ? count : 0;
    }

    /**
     * Calculate total time for an event (sum of totalParticipants * duration for each activity)
     *
     * @param eventId the event ID
     * @return total time in minutes
     */
    public Integer calculateTotalTime(Integer eventId) {
        List<Activity> activities = activityRepository.findByEventId(eventId);
        int totalTime = 0;
        
        for (Activity activity : activities) {
            Integer activityParticipants = userActivityRepository.countSignedUpParticipantsByActivityId(activity.getId());
            if (activityParticipants != null && activity.getDuration() != null) {
                totalTime += activityParticipants * activity.getDuration();
            }
        }
        
        return totalTime;
    }

    /**
     * Calculate total amount for an event (sum of donation amounts from activities with templateId = 2)
     *
     * @param eventId the event ID
     * @return total donation amount
     */
    public BigDecimal calculateTotalAmount(Integer eventId) {
        List<Activity> activities = activityRepository.findByEventId(eventId);
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // Collect all activity IDs that have templateId = 2 (donation activities)
        List<Integer> donationActivityIds = activities.stream()
                .filter(activity -> activity.getTemplateId() != null && activity.getTemplateId().equals(2))
                .map(Activity::getId)
                .collect(Collectors.toList());
        
        // If no donation activities, return zero
        if (donationActivityIds.isEmpty()) {
            return totalAmount;
        }
        
        // Get all user activities for donation activities with SIGNED_UP state in one database call
        List<UserActivity> userActivities = userActivityRepository.findByActivityIdInAndState(donationActivityIds, "SIGNED_UP");
        
        // Process all user activities to sum up the amounts
        for (UserActivity userActivity : userActivities) {
            try {
                BasicDetailDTO detail = activityDetailFactory.createDetail(2, userActivity.getDetail());
                if (detail != null && detail instanceof DonationDetailDTO) {
                    DonationDetailDTO donationDetail = (DonationDetailDTO) detail;
                    BigDecimal amount = donationDetail.getAmount();
                    
                    if (amount != null) {
                        totalAmount = totalAmount.add(amount);
                    }
                }
            } catch (Exception e) {
                // Skip invalid detail conversion errors and continue processing
                log.warn("Failed to process donation detail for user activity {}: {}", userActivity.getId(), e.getMessage());
                continue;
            }
        }
        
        return totalAmount;
    }
}
