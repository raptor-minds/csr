package com.blockchain.csr.controller;

import com.blockchain.csr.model.dto.*;
import com.blockchain.csr.model.entity.Event;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.model.enums.ActivityStatus;
import com.blockchain.csr.repository.EventRepository;
import com.blockchain.csr.repository.ActivityRepository;
import com.blockchain.csr.service.EventService;
import com.blockchain.csr.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.validation.Valid;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventRepository eventRepository;
    private final ActivityRepository activityRepository;
    private final EventService eventService;
    private final ActivityService activityService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Calculate activity status based on current time vs start/end times
     * 
     * @param startTime the activity start time
     * @param endTime the activity end time
     * @return the calculated status string
     */
    private String calculateActivityStatus(LocalDateTime startTime, LocalDateTime endTime) {
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
     * Calculate event status based on current time vs start/end times
     * 
     * @param startTime the event start time
     * @param endTime the event end time
     * @return the calculated status string
     */
    private String calculateEventStatus(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) {
            return "NOT_STARTED";
        }
        
        Date now = new Date();
        
        if (now.before(startTime)) {
            return "NOT_STARTED";
        } else if (now.after(endTime)) {
            return "FINISHED";
        } else {
            return "IN_PROGRESS";
        }
    }

    @GetMapping
    public ResponseEntity<BaseResponse<EventListResponse>> getEvents(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "eventName", required = false) String eventName
    ) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Event> eventPage;
        
        // Filter by event name if provided
        if (eventName != null && !eventName.trim().isEmpty()) {
            eventPage = eventRepository.findByNameContainingIgnoreCase(eventName.trim(), pageable);
        } else {
            eventPage = eventRepository.findAll(pageable);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        List<EventWithActivitiesDto> eventList = eventPage.getContent().stream().map(event -> {
            List<ActivityDto> activities = activityRepository.findByEventId(event.getId()).stream().map(activity -> {
                ActivityDto.ActivityDtoBuilder activityBuilder = ActivityDto.builder()
                        .id(activity.getId())
                        .name(activity.getName())
                        .description(activity.getDescription())
                        .startTime(activity.getStartTime() != null ? activity.getStartTime().format(DATE_TIME_FORMATTER) : null)
                        .endTime(activity.getEndTime() != null ? activity.getEndTime().format(DATE_TIME_FORMATTER) : null)
                        .status(calculateActivityStatus(activity.getStartTime(), activity.getEndTime()))
                        .createdAt(activity.getCreatedAt() != null ? activity.getCreatedAt().format(DATE_TIME_FORMATTER) : null);
                
                // Add enhanced fields for activities if requested
                Integer activityTotalParticipants = activityService.getTotalParticipants(activity.getId());
                Integer activityTotalTime = activityService.calculateTotalTime(activity, activityTotalParticipants);
                activityBuilder.totalParticipants(activityTotalParticipants)
                              .totalTime(activityTotalTime);
                
                return activityBuilder.build();
            }).collect(Collectors.toList());
            
            EventWithActivitiesDto.EventWithActivitiesDtoBuilder builder = EventWithActivitiesDto.builder()
                    .id(event.getId())
                    .name(event.getName())
                    .startTime(event.getStartTime() != null ? sdf.format(event.getStartTime()) : null)
                    .endTime(event.getEndTime() != null ? sdf.format(event.getEndTime()) : null)
                    .status(calculateEventStatus(event.getStartTime(), event.getEndTime()))
                    .isDisplay(true) // 需补充字段
                    .bgImage(event.getAvatar())
                    .activities(activities)
                    .createdAt(event.getCreatedAt() != null ? sdf.format(event.getCreatedAt()) : null)
                    .detailImage(event.getDetailImage());
            
            // Add enhanced fields if requested
            Integer totalParticipants = eventService.getTotalParticipants(event.getId());
            Integer totalTime = eventService.calculateTotalTime(event.getId());
            java.math.BigDecimal totalAmount = eventService.calculateTotalAmount(event.getId());
            builder.totalParticipants(totalParticipants)
                   .totalTime(totalTime)
                   .totalAmount(totalAmount);
            
            return builder.build();
        }).collect(Collectors.toList());
        
        EventListResponse response = EventListResponse.builder()
                .data(eventList)
                .total(eventPage.getTotalElements())
                .page(page)
                .pageSize(pageSize)
                .build();
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<EventDetailDto>> getEventDetail(@PathVariable Integer id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            return ResponseEntity.ok(BaseResponse.error(404, "事件不存在"));
        }
        List<String> visibleLocations = List.of();
        List<String> visibleRoles = List.of();
        try {
            if (event.getVisibleLocations() != null) {
                visibleLocations = objectMapper.readValue(event.getVisibleLocations(), new TypeReference<List<String>>() {});
            }
            if (event.getVisibleRoles() != null) {
                visibleRoles = objectMapper.readValue(event.getVisibleRoles(), new TypeReference<List<String>>() {});
            }
        } catch (Exception e) {
            visibleLocations = List.of("未知");
            visibleRoles = List.of("未知");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        EventDetailDto detail = EventDetailDto.builder()
                .id(event.getId())
                .name(event.getName())
                .startTime(event.getStartTime() != null ? sdf.format(event.getStartTime()) : null)
                .endTime(event.getEndTime() != null ? sdf.format(event.getEndTime()) : null)
                .status(calculateEventStatus(event.getStartTime(), event.getEndTime()))
                .icon(event.getAvatar())
                .description(event.getDescription())
                .isDisplay(event.getIsDisplay() != null ? event.getIsDisplay() : false)
                .visibleLocations(visibleLocations)
                .visibleRoles(visibleRoles)
                .createdAt(event.getCreatedAt() != null ? sdf.format(event.getCreatedAt()) : null)
                .detailImage(event.getDetailImage())
                .build();
        return ResponseEntity.ok(BaseResponse.success(detail));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createEvent(@Valid @RequestBody EventCreateRequest request) {
        try {
            // Validate that endTime is after startTime
            if (!request.isEndTimeAfterStartTime()) {
                return ResponseEntity.ok(BaseResponse.error(400, "结束时间必须晚于开始时间"));
            }
            
            Event event = new Event();
            event.setName(request.getName());
            event.setStartTime(request.getStartTime());
            event.setEndTime(request.getEndTime());
            event.setAvatar(request.getIcon());
            event.setDescription(request.getDescription());
            event.setIsDisplay(request.getIsDisplay());
            event.setVisibleLocations(objectMapper.writeValueAsString(request.getVisibleLocations()));
            event.setVisibleRoles(objectMapper.writeValueAsString(request.getVisibleRoles()));
            event.setDetailImage(request.getDetailImage());
            // Set created_at to current system time
            event.setCreatedAt(new Date());
            eventRepository.save(event);
            return ResponseEntity.ok(BaseResponse.success("事件创建成功"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.internalError("事件创建失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> updateEvent(@PathVariable Integer id, @Valid @RequestBody EventCreateRequest request) {
        try{
            // Validate that endTime is after startTime
            if (!request.isEndTimeAfterStartTime()) {
                return ResponseEntity.ok(BaseResponse.error(400, "结束时间必须晚于开始时间"));
            }
            
            Event event = eventRepository.findById(id).orElse(null);
            if (event == null) {
                return ResponseEntity.ok(BaseResponse.error(404, "事件不存在"));
            }
            event.setName(request.getName());
            event.setStartTime(request.getStartTime());
            event.setEndTime(request.getEndTime());
            event.setAvatar(request.getIcon());
            event.setDescription(request.getDescription());
            event.setIsDisplay(request.getIsDisplay());
            event.setVisibleLocations(objectMapper.writeValueAsString(request.getVisibleLocations()));
            event.setVisibleRoles(objectMapper.writeValueAsString(request.getVisibleRoles()));
            event.setDetailImage(request.getDetailImage());
            eventRepository.save(event);
            return ResponseEntity.ok(BaseResponse.success("更新事件成功"));

        }catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.internalError("更新事件失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/display")
    public ResponseEntity<BaseResponse<Object>> updateEventDisplay(@PathVariable Integer id, @Valid @RequestBody EventDisplayUpdateRequest request) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            return ResponseEntity.ok(BaseResponse.error(404, "事件不存在"));
        }
        event.setIsDisplay(request.getIsDisplay());
        eventRepository.save(event);
        return ResponseEntity.ok(BaseResponse.success("更新事件显示状态成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteEvent(@PathVariable Integer id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            return ResponseEntity.ok(BaseResponse.error(404, "事件不存在"));
        }
        // 删除关联活动
        List<Activity> activities = activityRepository.findByEventId(id);
        activityRepository.deleteAll(activities);
        // 删除事件
        eventRepository.delete(event);
        return ResponseEntity.ok(BaseResponse.success("删除成功"));
    }
} 