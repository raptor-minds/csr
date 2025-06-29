package com.blockchain.csr.model.mapper;

import com.blockchain.csr.model.dto.ActivityRequestDto;
import com.blockchain.csr.model.dto.ActivityResponseDto;
import com.blockchain.csr.model.entity.Activity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivityMapper {

    public Activity toEntity(ActivityRequestDto dto) {
        Activity entity = new Activity();
        entity.setName(dto.getName());
        entity.setEventId(dto.getEventId());
        entity.setTemplateId(dto.getTemplateId());
        entity.setTotalTime(dto.getTotalTime());
        entity.setIcon(dto.getIcon());
        entity.setDescription(dto.getDescription());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setStatus(dto.getStatus());
        entity.setVisibleLocations(convertToJson(dto.getVisibleLocations()));
        entity.setVisibleRoles(convertToJson(dto.getVisibleRoles()));
        return entity;
    }

    public ActivityResponseDto toResponseDto(Activity entity) {
        ActivityResponseDto dto = new ActivityResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEventId(entity.getEventId());
        dto.setTemplateId(entity.getTemplateId());
        dto.setTotalTime(entity.getTotalTime());
        dto.setIcon(entity.getIcon());
        dto.setDescription(entity.getDescription());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setStatus(entity.getStatus());
        dto.setVisibleLocations(convertToList(entity.getVisibleLocations()));
        dto.setVisibleRoles(convertToList(entity.getVisibleRoles()));
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
        if (dto.getTotalTime() != null) entity.setTotalTime(dto.getTotalTime());
        if (dto.getIcon() != null) entity.setIcon(dto.getIcon());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getStartTime() != null) entity.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) entity.setEndTime(dto.getEndTime());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getVisibleLocations() != null) entity.setVisibleLocations(convertToJson(dto.getVisibleLocations()));
        if (dto.getVisibleRoles() != null) entity.setVisibleRoles(convertToJson(dto.getVisibleRoles()));
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