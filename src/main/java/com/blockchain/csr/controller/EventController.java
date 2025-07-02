package com.blockchain.csr.controller;

import com.blockchain.csr.model.dto.*;
import com.blockchain.csr.model.entity.Event;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.repository.EventRepository;
import com.blockchain.csr.repository.ActivityRepository;
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

import jakarta.validation.Valid;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventRepository eventRepository;
    private final ActivityRepository activityRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public ResponseEntity<BaseResponse<EventListResponse>> getEvents(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Event> eventPage = eventRepository.findAll(pageable);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<EventWithActivitiesDto> eventList = eventPage.getContent().stream().map(event -> {
            List<ActivityDto> activities = activityRepository.findByEventId(event.getId()).stream().map(activity ->
                ActivityDto.builder()
                        .id(activity.getId())
                        .name(activity.getName())
                        .description(activity.getDescription())
                        .startTime(null) // 需补充字段
                        .endTime(null)   // 需补充字段
                        .status(null)    // 需补充字段
                        .build()
            ).collect(Collectors.toList());
            return EventWithActivitiesDto.builder()
                    .id(event.getId())
                    .name(event.getName())
                    .startTime(event.getStartTime() != null ? sdf.format(event.getStartTime()) : null)
                    .endTime(event.getEndTime() != null ? sdf.format(event.getEndTime()) : null)
                    .isDisplay(true) // 需补充字段
                    .bgImage(event.getAvatar())
                    .activities(activities)
                    .build();
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
            // 反序列化失败时返回"未知"
            visibleLocations = List.of("未知");
            visibleRoles = List.of("未知");
        }
        EventDetailDto detail = EventDetailDto.builder()
                .id(event.getId())
                .name(event.getName())
                .totalTime(event.getDuration())
                .icon(event.getAvatar())
                .description(event.getDescription())
                .isDisplay(event.getIsDisplay() != null ? event.getIsDisplay() : false)
                .visibleLocations(visibleLocations)
                .visibleRoles(visibleRoles)
                .build();
        return ResponseEntity.ok(BaseResponse.success(detail));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createEvent(@Valid @RequestBody EventCreateRequest request) {
        try {
            Event event = new Event();
            event.setName(request.getName());
            event.setDuration(request.getTotalTime());
            event.setAvatar(request.getIcon());
            event.setDescription(request.getDescription());
            event.setIsDisplay(request.getIsDisplay());
            event.setVisibleLocations(objectMapper.writeValueAsString(request.getVisibleLocations()));
            event.setVisibleRoles(objectMapper.writeValueAsString(request.getVisibleRoles()));
            eventRepository.save(event);
            return ResponseEntity.ok(BaseResponse.success("事件创建成功"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BaseResponse.internalError("事件创建失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> updateEvent(@PathVariable Integer id, @Valid @RequestBody EventCreateRequest request) {
        try{
            Event event = eventRepository.findById(id).orElse(null);
            if (event == null) {
                return ResponseEntity.ok(BaseResponse.error(404, "事件不存在"));
            }
            event.setName(request.getName());
            event.setDuration(request.getTotalTime());
            event.setAvatar(request.getIcon());
            event.setDescription(request.getDescription());
            event.setIsDisplay(request.getIsDisplay());
            event.setVisibleLocations(objectMapper.writeValueAsString(request.getVisibleLocations()));
            event.setVisibleRoles(objectMapper.writeValueAsString(request.getVisibleRoles()));
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