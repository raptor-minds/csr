package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blockchain.csr.repository.UserEventRepository;
import com.blockchain.csr.model.entity.UserEvent;
import com.blockchain.csr.model.dto.UserEventDto;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserEventService{

    private final UserEventRepository userEventRepository;
    private static final Logger log = LoggerFactory.getLogger(UserEventService.class);

    public int deleteByPrimaryKey(Integer id) {
        userEventRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(UserEvent record) {
        userEventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(UserEvent record) {
        userEventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public UserEvent selectByPrimaryKey(Integer id) {
        return userEventRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(UserEvent record) {
        userEventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(UserEvent record) {
        userEventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public List<UserEventDto> getUserEvents(Integer userId) {
        try {
            log.info("Fetching events for user ID: {}", userId);
            
            List<Object[]> results = userEventRepository.findUserEventsWithInfo(userId);
            log.info("Found {} events for user ID: {}", results.size(), userId);
            
            if (results.isEmpty()) {
                log.warn("No events found for user ID: {}. This could mean:", userId);
                log.warn("1. User ID {} does not exist", userId);
                log.warn("2. User ID {} exists but has no associated events", userId);
                log.warn("3. UserEvent table has no records for user ID {}", userId);
            }
            
            return results.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching events for user ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user events", e);
        }
    }

    private UserEventDto convertToDto(Object[] arr) {
        Integer id = (Integer) arr[0];
        String name = (String) arr[1];
        String type = arr[2] != null ? arr[2].toString() : null;
        Integer totalTime = arr[3] != null ? (Integer) arr[3] : null;
        Date endTime = arr[4] != null ? (Date) arr[4] : null;
        String eventStatus = arr.length > 5 && arr[5] != null ? arr[5].toString() : null;

        String typeStr = "线下事件";
        if ("online".equalsIgnoreCase(type)) typeStr = "线上事件";
        else if ("hybrid".equalsIgnoreCase(type)) typeStr = "混合事件";

        String duration = (totalTime != null ? (totalTime / 60) + "小时" : "未知");
        String status;
        if (eventStatus != null) {
            status = eventStatus;
        } else {
            status = (endTime != null && endTime.after(new Date())) ? "active" : "ended";
        }

        return UserEventDto.builder()
                .id(id)
                .name(name)
                .type(typeStr)
                .duration(duration)
                .status(status)
                .build();
    }

}
