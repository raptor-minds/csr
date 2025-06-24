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
import java.util.Map;

@RestController
public class TestController {

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
}
