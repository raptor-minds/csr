package com.blockchain.csr.model.mapper;

import com.blockchain.csr.model.dto.ActivityRequestDto;
import com.blockchain.csr.model.dto.ActivityResponseDto;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.model.enums.ActivityStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class ActivityMapper {

    public Activity toEntity(ActivityRequestDto dto) {
        Activity entity = new Activity();
        entity.setName(dto.getName());
        entity.setEventId(dto.getEventId());
        entity.setTemplateId(dto.getTemplateId());
        entity.setDuration(dto.getDuration());
        entity.setIcon(dto.getIcon());
        entity.setDescription(dto.getDescription());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setVisibleLocations(convertToJson(dto.getVisibleLocations()));
        entity.setVisibleRoles(convertToJson(dto.getVisibleRoles()));
        entity.setImage1(dto.getImage1());
        entity.setImage2(dto.getImage2());
        return entity;
    }

    public ActivityResponseDto toResponseDto(Activity entity) {
        ActivityResponseDto dto = new ActivityResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEventId(entity.getEventId());
        dto.setTemplateId(entity.getTemplateId());
        dto.setDuration(entity.getDuration());
        dto.setIcon(entity.getIcon());
        dto.setDescription(entity.getDescription());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setStatus(calculateActivityStatus(entity.getStartTime(), entity.getEndTime()));
        dto.setVisibleLocations(convertToList(entity.getVisibleLocations()));
        dto.setVisibleRoles(convertToList(entity.getVisibleRoles()));
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setImage1(entity.getImage1());
        dto.setImage2(entity.getImage2());
        return dto;
    }

    /**
     * Calculate activity status based on current time vs start/end times
     * 
     * @param startTime the activity start time
     * @param endTime the activity end time
     * @return the calculated status string
     */
    public String calculateActivityStatus(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return ActivityStatus.NOT_STARTED.getValue();
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isBefore(startTime)) {
            return ActivityStatus.NOT_STARTED.getValue();
        } else if (now.isAfter(endTime)) {
            return ActivityStatus.FINISHED.getValue();
        } else {
            return ActivityStatus.IN_PROGRESS.getValue();
        }
    }

    /**
     * Convert Activity entity to ActivityResponseDto with enhanced fields
     *
     * @param entity the Activity entity
     * @param totalParticipants the total number of participants
     * @param totalTime the total time (participants * duration)
     * @return ActivityResponseDto with enhanced fields
     */
    public ActivityResponseDto toResponseDtoWithEnhancedFields(Activity entity, Integer totalParticipants, Integer totalTime) {
        ActivityResponseDto dto = toResponseDto(entity);
        dto.setTotalParticipants(totalParticipants);
        dto.setTotalTime(totalTime);
        return dto;
    }

    // 保留原有JSON转换方法
    ObjectMapper objectMapper = new ObjectMapper();

    // 使用预定义常量
    TypeReference<List<String>> LIST_OF_STRINGS = new TypeReference<>() {};

    public void updateEntityFromDto(ActivityRequestDto dto, Activity entity) {
        if (dto == null) return;
        
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getEventId() != null) entity.setEventId(dto.getEventId());
        if (dto.getTemplateId() != null) entity.setTemplateId(dto.getTemplateId());
        if (dto.getDuration() != null) entity.setDuration(dto.getDuration());
        if (dto.getIcon() != null) entity.setIcon(dto.getIcon());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getStartTime() != null) entity.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) entity.setEndTime(dto.getEndTime());
        if (dto.getVisibleLocations() != null) entity.setVisibleLocations(convertToJson(dto.getVisibleLocations()));
        if (dto.getVisibleRoles() != null) entity.setVisibleRoles(convertToJson(dto.getVisibleRoles()));
        if (dto.getImage1() != null) entity.setImage1(dto.getImage1());
        if (dto.getImage2() != null) entity.setImage2(dto.getImage2());
    }

    String convertToJson(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    List<String> convertToList(String json) {
        try {
            return objectMapper.readValue(json, LIST_OF_STRINGS);
        } catch (Exception e) {
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }
}