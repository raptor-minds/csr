package com.blockchain.csr.controller;

import com.blockchain.csr.model.dto.ActivityRequestDto;
import com.blockchain.csr.model.dto.ActivityResponseDto;
import com.blockchain.csr.model.dto.ActivitySignupRequest;
import com.blockchain.csr.model.mapper.ActivityMapper;
import com.blockchain.csr.service.ActivityService;
import com.blockchain.csr.config.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.model.dto.BaseResponse;
import com.blockchain.csr.model.dto.UserActivityDto;

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

    @Autowired
    private SecurityUtils securityUtils;

    // 获取活动列表
    @GetMapping
    public ResponseEntity<BaseResponse<List<?>>> getActivities(
            @RequestParam(required = false) Integer eventId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false, defaultValue = "false") Boolean needsTotal) {
        List<Activity> activities;
        if(userId != null) {
            List<UserActivityDto> details = activityService.getUserActivitiesByEvent(userId, eventId, page, pageSize);
            return ResponseEntity.ok(BaseResponse.success(details));
        }else {
            activities = activityService.getActivities(eventId, page, pageSize);
        }

        List<ActivityResponseDto> dtos;
        if (needsTotal != null && needsTotal) {
            // 如果需要总计信息，计算每个活动的总参与人数和总时间
            dtos = activities.stream()
                .map(activity -> {
                    Integer totalParticipants = activityService.getTotalParticipants(activity.getId());
                    Integer totalTime = activityService.calculateTotalTime(activity, totalParticipants);
                    return activityMapper.toResponseDtoWithEnhancedFields(activity, totalParticipants, totalTime);
                })
                .collect(Collectors.toList());
        } else {
            // 普通模式，不计算总计信息
            dtos = activities.stream()
                .map(activityMapper::toResponseDto)
                .collect(Collectors.toList());
        }
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
    public ResponseEntity<BaseResponse<Object>> createActivity(@RequestBody @Validated(value = {ActivityRequestDto.CreateGroup.class}) ActivityRequestDto dto) {
        Activity activity = activityMapper.toEntity(dto);
        Integer activityId = activityService.createActivity(activity);
        
        // 创建包含活动ID和提示信息的响应对象
        java.util.Map<String, Object> responseData = new java.util.HashMap<>();
        responseData.put("activityId", activityId);
        responseData.put("message", "活动创建成功");
        
        return ResponseEntity.ok(BaseResponse.success(responseData));
    }

    // 更新活动
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> updateActivity(
            @PathVariable Integer id, 
            @RequestBody @Validated(value = {ActivityRequestDto.UpdateGroup.class}) ActivityRequestDto dto) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body(BaseResponse.error("无效的活动ID"));
        }
        Activity existingActivity = activityService.getActivityById(id);
        if (existingActivity == null) {
            return ResponseEntity.badRequest().body(BaseResponse.error("活动不存在"));
        }
        activityMapper.updateEntityFromDto(dto, existingActivity);
        activityService.updateActivity(existingActivity);
        return ResponseEntity.ok(BaseResponse.success("活动更新成功"));
    }

    // 删除活动
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteActivity(@PathVariable Integer id) {
        // 实现逻辑：调用activityService删除活动
        activityService.deleteActivity(id);
        return ResponseEntity.ok(BaseResponse.success("活动删除成功"));
    }

    // 用户报名活动
    @PostMapping("/{activityId}/signup")
    public ResponseEntity<BaseResponse<Object>> signupActivity(
            @PathVariable Integer activityId,
            @RequestBody @Validated ActivitySignupRequest request) {
        
        // 鉴权：允许管理员操作，或者允许用户本身操作
        Integer currentUserId = securityUtils.getCurrentUserId();
        boolean canSignup = SecurityUtils.isAdmin() || 
                           (currentUserId != null && currentUserId.equals(request.getUserId()));
        
        if (!canSignup) {
            return ResponseEntity.status(403).body(BaseResponse.forbidden("Access denied. You can only signup for yourself."));
        }
        
        try {
            activityService.signupActivity(activityId, request.getUserId());
            return ResponseEntity.ok(BaseResponse.success("活动报名成功"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(BaseResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.internalError("服务器内部错误"));
        }
    }

    // 用户退出活动
    @PostMapping("/{activityId}/withdraw")
    public ResponseEntity<BaseResponse<Object>> withdrawActivity(
            @PathVariable Integer activityId,
            @RequestBody @Validated ActivitySignupRequest request) {
        
        // 鉴权：允许管理员操作，或者允许用户本身操作
        Integer currentUserId = securityUtils.getCurrentUserId();
        boolean canWithdraw = SecurityUtils.isAdmin() || 
                             (currentUserId != null && currentUserId.equals(request.getUserId()));
        
        if (!canWithdraw) {
            return ResponseEntity.status(403).body(BaseResponse.forbidden("Access denied. You can only withdraw for yourself."));
        }
        
        try {
            activityService.withdrawActivity(activityId, request.getUserId());
            return ResponseEntity.ok(BaseResponse.success("活动退出成功"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(BaseResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.internalError("服务器内部错误"));
        }
    }
    
}