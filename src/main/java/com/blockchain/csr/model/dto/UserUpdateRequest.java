package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 45, message = "Username cannot exceed 45 characters")
    private String username;
    
    @NotBlank(message = "Role cannot be blank")
    @Pattern(regexp = "^(admin|user)$", message = "Role must be either 'admin' or 'user'")
    private String role;
    
    @NotBlank(message = "Location cannot be blank")
    @Pattern(regexp = "^(SH|SZ)$", message = "Location must be either 'SH' or 'SZ'")
    private String location;
} 