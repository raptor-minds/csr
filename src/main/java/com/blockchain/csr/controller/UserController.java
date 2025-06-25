package com.blockchain.csr.controller;

import com.blockchain.csr.model.dto.BaseResponse;
import com.blockchain.csr.model.dto.PasswordResetRequest;
import com.blockchain.csr.model.dto.UserDto;
import com.blockchain.csr.model.dto.UserListResponse;
import com.blockchain.csr.model.dto.UserActivityDto;
import com.blockchain.csr.model.dto.UserUpdateRequest;
import com.blockchain.csr.model.dto.ReviewerUpdateRequest;
import com.blockchain.csr.model.dto.BatchDeleteRequest;
import com.blockchain.csr.model.dto.EventDto;
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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @securityUtils.isCurrentUser(#id))")
    public ResponseEntity<BaseResponse<UserDto>> getUserDetails(@PathVariable Integer id) {
        try {
            log.info("Requesting user details for ID: {}", id);
            UserDto userDetails = userService.getUserDetails(id);
            return ResponseEntity.ok(BaseResponse.success(userDetails));
        } catch (IllegalArgumentException e) {
            log.warn("User not found for ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(404).body(BaseResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting user details for ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(BaseResponse.internalError("Failed to retrieve user details"));
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

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Object>> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        try {
            log.info("Admin requesting to update user ID: {} with data: {}", id, updateRequest);
            userService.updateUser(id, updateRequest);
            return ResponseEntity.ok(BaseResponse.success("Update user successful"));
        } catch (IllegalArgumentException e) {
            log.warn("Update user failed for ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(BaseResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during user update for ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(BaseResponse.internalError("An unexpected error occurred"));
        }
    }

    @PutMapping("/{id}/reviewer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Object>> changeReviewer(
            @PathVariable Integer id,
            @Valid @RequestBody ReviewerUpdateRequest reviewerRequest) {
        try {
            log.info("Admin requesting to change reviewer for user ID: {} to reviewer ID: {}", 
                    id, reviewerRequest.getReviewerId());
            userService.changeReviewer(id, reviewerRequest.getReviewerId());
            return ResponseEntity.ok(BaseResponse.success("Change reviewer successful"));
        } catch (IllegalArgumentException e) {
            log.warn("Change reviewer failed for user ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(BaseResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during reviewer change for user ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(BaseResponse.internalError("An unexpected error occurred"));
        }
    }

    @DeleteMapping("/batch-delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Object>> batchDeleteUsers(
            @Valid @RequestBody BatchDeleteRequest batchDeleteRequest) {
        try {
            log.info("Admin requesting to batch delete users with IDs: {}", batchDeleteRequest.getUserIds());
            userService.batchDeleteUsers(batchDeleteRequest.getUserIds());
            return ResponseEntity.ok(BaseResponse.success("Batch deletion success."));
        } catch (IllegalArgumentException e) {
            log.warn("Batch delete failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(BaseResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during batch delete: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(BaseResponse.internalError("An unexpected error occurred"));
        }
    }

    @GetMapping("/{id}/events")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @securityUtils.isCurrentUser(#id))")
    public ResponseEntity<BaseResponse<List<EventDto>>> getUserEvents(@PathVariable Integer id) {
        try {
            log.info("Requesting events for user ID: {}", id);
            List<EventDto> events = userEventService.getEventsByUserId(id);
            return ResponseEntity.ok(BaseResponse.success(events));
        } catch (IllegalArgumentException e) {
            log.warn("Failed to get events for user ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(404).body(BaseResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting events for user ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(BaseResponse.internalError("Failed to retrieve user events"));
        }
    }

    @GetMapping("/{id}/activities")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @securityUtils.isCurrentUser(#id))")
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
} 