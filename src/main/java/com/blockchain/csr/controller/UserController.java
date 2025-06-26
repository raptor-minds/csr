package com.blockchain.csr.controller;

import com.blockchain.csr.model.dto.BaseResponse;
import com.blockchain.csr.model.dto.PasswordResetRequest;
import com.blockchain.csr.model.dto.UserListResponse;
import com.blockchain.csr.model.dto.UserActivityDto;
import com.blockchain.csr.model.dto.UserEventDto;
import com.blockchain.csr.service.UserService;
import com.blockchain.csr.service.UserActivityService;
import com.blockchain.csr.service.UserEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserActivityService userActivityService;
    private final UserEventService userEventService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<UserListResponse>> getUserList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        try {
            log.info("Admin requesting user list with params - page: {}, pageSize: {}, username: {}, sortField: {}, sortOrder: {}", 
                    page, pageSize, username, sortField, sortOrder);
            
            UserListResponse userList = userService.getUserList(page, pageSize, username, sortField, sortOrder);
            return ResponseEntity.ok(BaseResponse.success(userList));
        } catch (Exception e) {
            log.error("Error getting user list: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(BaseResponse.internalError("Failed to retrieve user list"));
        }
    }

    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Object>> resetPassword(
            @PathVariable Integer id,
            @Valid @RequestBody PasswordResetRequest request) {
        try {
            log.info("Admin requesting password reset for user ID: {}", id);
            userService.resetPassword(id, request.getPassword());
            return ResponseEntity.ok(BaseResponse.success("Password reset successful."));
        } catch (IllegalArgumentException e) {
            log.warn("Password reset failed for user ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(BaseResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during password reset for user ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(BaseResponse.internalError("An unexpected error occurred"));
        }
    }

    @GetMapping("/{id}/activities")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<List<UserActivityDto>>> getUserActivities(@PathVariable Integer id) {
        try {
            log.info("Requesting activities for user ID: {}", id);
            
            List<UserActivityDto> activities = userActivityService.getUserActivities(id);
            
            return ResponseEntity.ok(BaseResponse.success(activities));
        } catch (Exception e) {
            log.error("Error getting activities for user ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(BaseResponse.internalError("Failed to retrieve user activities"));
        }
    }

    @GetMapping("/{id}/events")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<List<UserEventDto>>> getUserEvents(@PathVariable Integer id) {
        try {
            log.info("Requesting events for user ID: {}", id);
            List<UserEventDto> events = userEventService.getUserEvents(id);
            return ResponseEntity.ok(BaseResponse.success(events));
        } catch (Exception e) {
            log.error("Error getting events for user ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(BaseResponse.internalError("Failed to retrieve user events"));
        }
    }
} 