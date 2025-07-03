package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.blockchain.csr.model.entity.Event;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.repository.EventRepository;
import com.blockchain.csr.repository.ActivityRepository;
import com.blockchain.csr.repository.UserActivityRepository;

import java.util.List;
import java.util.Date;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class EventService{

    private final EventRepository eventRepository;
    private final ActivityRepository activityRepository;
    private final UserActivityRepository userActivityRepository;

    
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
}
