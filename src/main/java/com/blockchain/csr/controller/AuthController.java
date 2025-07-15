package com.blockchain.csr.controller;

import com.blockchain.csr.config.JwtUtil;
import com.blockchain.csr.model.dto.AuthRequest;
import com.blockchain.csr.model.dto.AuthResponse;
import com.blockchain.csr.model.dto.BaseResponse;
import com.blockchain.csr.model.dto.RefreshTokenRequest;
import com.blockchain.csr.model.dto.RefreshTokenResponse;
import com.blockchain.csr.model.entity.User;
import com.blockchain.csr.service.RefreshTokenService;
import com.blockchain.csr.service.UserService;
import com.blockchain.csr.service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Object>> register(@Valid @RequestBody AuthRequest request) {
        try {
            userService.createUser(request.getUsername(), request.getPassword(), 
                                   request.getNickname(), request.getRealName(), request.getGender());
            return ResponseEntity.ok(BaseResponse.success("User registered successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(BaseResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<BaseResponse<Object>> registerAdmin(@Valid @RequestBody AuthRequest request) {
        try {
            // In a real application, you'd want to restrict this endpoint to existing admins
            // For now, allowing admin creation for initial setup
            userService.createAdminUser(request.getUsername(), request.getPassword(),
                                       request.getNickname(), request.getRealName(), request.getGender());
            return ResponseEntity.ok(BaseResponse.success("Admin user registered successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(BaseResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        User user = userService.getUserByUsername(request.getUsername());
        return generateAuthResponse(request, user);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<BaseResponse<AuthResponse>> adminLogin(@RequestBody AuthRequest request) {
        // 查询用户角色
        User user = userService.getUserByUsername(request.getUsername());
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body(BaseResponse.forbidden("无权登录后台"));
        }
        return generateAuthResponse(request, user);
    }

    private ResponseEntity<BaseResponse<AuthResponse>> generateAuthResponse(AuthRequest request, User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            // Store the refresh token
            refreshTokenService.addRefreshToken(request.getUsername(), refreshToken);

//            user = userService.getUserByUsername(request.getUsername());

            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(300L) // 5 minutes in seconds
                    .id(user.getId())
                    .username(user.getUsername())
                    .build();
            
            return ResponseEntity.ok(BaseResponse.success(authResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(BaseResponse.unauthorized("Invalid username or password"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Object>> logout(@RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            if (refreshToken != null && jwtUtil.validateToken(refreshToken) && jwtUtil.isRefreshToken(refreshToken)) {
                refreshTokenService.removeRefreshToken(refreshToken);
            }
            return ResponseEntity.ok(BaseResponse.success("Logged out successfully"));
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            return ResponseEntity.ok(BaseResponse.success("Logged out successfully")); // Return success even if token is invalid
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<RefreshTokenResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            
            if (refreshToken == null || !jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
                return ResponseEntity.status(401).body(BaseResponse.unauthorized("Invalid refresh token"));
            }
            
            if (!refreshTokenService.isValidRefreshToken(refreshToken)) {
                return ResponseEntity.status(401).body(BaseResponse.unauthorized("Refresh token not found or expired"));
            }
            
            String username = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtUtil.generateAccessToken(userDetails);
            
            RefreshTokenResponse response = RefreshTokenResponse.builder()
                    .accessToken(newAccessToken)
                    .tokenType("Bearer")
                    .expiresIn(300L) // 5 minutes in seconds
                    .build();
            
            return ResponseEntity.ok(BaseResponse.success(response));
        } catch (Exception e) {
            log.error("Error during token refresh: {}", e.getMessage());
            return ResponseEntity.status(401).body(BaseResponse.unauthorized("Token refresh failed"));
        }
    }
} 