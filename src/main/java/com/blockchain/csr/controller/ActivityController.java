package com.blockchain.csr.controller;

import com.blockchain.csr.model.dto.ActivityRequestDto;
import com.blockchain.csr.model.dto.ActivityResponseDto;
import com.blockchain.csr.model.mapper.ActivityMapper;
import com.blockchain.csr.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.model.dto.BaseResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangrucheng on 2025/5/19
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityMapper activityMapper;

    // 获取活动列表
    @GetMapping
    public ResponseEntity<BaseResponse<List<ActivityResponseDto>>> getActivities(
            @RequestParam(required = false) Integer eventId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        // 实现逻辑：调用activityService获取活动列表
        List<Activity> activities = activityService.getActivities(eventId, page, pageSize);
        List<ActivityResponseDto> dtos = activities.stream()
            .map(activityMapper::toResponseDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success(dtos));
    }

    // 获取活动详情
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ActivityResponseDto>> getActivityDetail(@PathVariable Integer id) {
        Activity activity = activityService.getActivityById(id);
        ActivityResponseDto dto = activityMapper.toResponseDto(activity);
        return ResponseEntity.ok(BaseResponse.success(dto));
    }

    // 创建活动
    @PostMapping
    public ResponseEntity<BaseResponse<Integer>> createActivity(@RequestBody @Validated(value = {ActivityRequestDto.CreateGroup.class}) ActivityRequestDto dto) {
        Activity activity = activityMapper.toEntity(dto);
        int activityId = activityService.createActivity(activity);
        return ResponseEntity.ok(BaseResponse.success(activityId));
    }

    // 更新活动
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> updateActivity(
            @PathVariable Integer id, 
            @RequestBody @Validated(value = {ActivityRequestDto.UpdateGroup.class}) ActivityRequestDto dto) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body(BaseResponse.error("Invalid activity ID"));
        }
        Activity activity = activityMapper.toEntity(dto);
        activity.setId(id);
        activityService.updateActivity(activity);
        return ResponseEntity.ok(BaseResponse.success());
    }

    // 删除活动
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteActivity(@PathVariable Integer id) {
        // 实现逻辑：调用activityService删除活动
        activityService.deleteActivity(id);
        return ResponseEntity.ok(BaseResponse.success());
    }
}