package com.blockchain.csr.controller;

import com.blockchain.csr.config.SecurityUtils;
import com.blockchain.csr.model.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.blockchain.csr.repository.UserRepository;
import com.blockchain.csr.repository.EventRepository;
import com.blockchain.csr.repository.UserEventRepository;
import com.blockchain.csr.model.entity.User;
import com.blockchain.csr.model.entity.Event;
import com.blockchain.csr.model.entity.UserEvent;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TestController {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserEventRepository userEventRepository;

    public TestController(UserRepository userRepository, EventRepository eventRepository, UserEventRepository userEventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.userEventRepository = userEventRepository;
    }

    @PostMapping("/testAuth")
    public ResponseEntity<BaseResponse<Map<String, Object>>> test(Authentication authentication) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", authentication.getName());
        data.put("role", SecurityUtils.getCurrentUserRole().getValue());
        data.put("isAdmin", SecurityUtils.isAdmin());
        data.put("authorities", authentication.getAuthorities());
        
        return ResponseEntity.ok(BaseResponse.success("Authentication successful", data));
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> adminTest() {
        String message = "Hello Admin! This endpoint is only accessible by ADMIN role.";
        return ResponseEntity.ok(BaseResponse.success("Admin access granted", message));
    }

    @GetMapping("/user/test")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> userTest() {
        String message = "Hello User! This endpoint is accessible by USER and ADMIN roles.";
        return ResponseEntity.ok(BaseResponse.success("User access granted", message));
    }

    @GetMapping("/profile")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getProfile() {
        Map<String, Object> profile = new HashMap<>();
        profile.put("username", SecurityUtils.getCurrentUsername());
        profile.put("role", SecurityUtils.getCurrentUserRole().getValue());
        profile.put("isAdmin", SecurityUtils.isAdmin());
        
        return ResponseEntity.ok(BaseResponse.success("Profile retrieved", profile));
    }

    @GetMapping("/db-test")
    @PreAuthorize("permitAll()")
    public ResponseEntity<BaseResponse<Object>> testDatabase() {
        try {
            // 检查用户数据
            List<User> users = userRepository.findAll();
            long userCount = userRepository.count();
            
            // 检查事件数据
            List<Event> events = eventRepository.findAll();
            long eventCount = eventRepository.count();
            
            // 检查用户事件关联数据
            List<UserEvent> userEvents = userEventRepository.findAll();
            long userEventCount = userEventRepository.count();
            
            // 检查用户ID为1的事件
            List<UserEvent> user1Events = userEventRepository.findByUserId(1);
            
            Map<String, Object> result = new HashMap<>();
            result.put("totalUsers", userCount);
            result.put("totalEvents", eventCount);
            result.put("totalUserEvents", userEventCount);
            result.put("user1Events", user1Events.size());
            result.put("users", users.stream().map(u -> Map.of("id", u.getId(), "username", u.getUsername())).collect(Collectors.toList()));
            result.put("events", events.stream().map(e -> Map.of("id", e.getId(), "name", e.getName())).collect(Collectors.toList()));
            
            return ResponseEntity.ok(BaseResponse.success(result));
        } catch (Exception e) {
            log.error("Database test failed: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(BaseResponse.internalError("Database test failed: " + e.getMessage()));
        }
    }
}
