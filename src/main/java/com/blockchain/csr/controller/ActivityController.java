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
        Activity existingActivity = activityService.getActivityById(id);
        if (existingActivity == null) {
            return ResponseEntity.badRequest().body(BaseResponse.error("Activity not found"));
        }
        activityMapper.updateEntityFromDto(dto, existingActivity);
        activityService.updateActivity(existingActivity);
        return ResponseEntity.ok(BaseResponse.success());
    }

    // 删除活动
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteActivity(@PathVariable Integer id) {
        // 实现逻辑：调用activityService删除活动
        activityService.deleteActivity(id);
        return ResponseEntity.ok(BaseResponse.success());
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
            return ResponseEntity.ok(BaseResponse.success("Signup successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(BaseResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.internalError("Internal server error"));
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
            return ResponseEntity.ok(BaseResponse.success("Withdraw successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(BaseResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.internalError("Internal server error"));
        }
    }

    // 获取用户在指定事件下参与的活动
    @GetMapping(params = {"userId", "eventId"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<BaseResponse<List<ActivityResponseDto>>> getUserActivitiesByEvent(
            @RequestParam Integer userId,
            @RequestParam Integer eventId) {
        
        // 权限检查：如果不是admin且不是当前用户，直接返回错误
        Integer currentUserId = securityUtils.getCurrentUserId();
        boolean canReview = SecurityUtils.isAdmin() ||
                (currentUserId != null && currentUserId.equals(userId));
        if (!canReview) {
            return ResponseEntity.status(403).body(BaseResponse.forbidden("您只能查看自己的活动信息"));
        }
        
        // 权限通过后，才执行数据库查询
        List<Activity> activities = activityService.getUserActivitiesByEvent(userId, eventId);
        List<ActivityResponseDto> dtos = activities.stream()
            .map(activityMapper::toResponseDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success(dtos));
    }
}