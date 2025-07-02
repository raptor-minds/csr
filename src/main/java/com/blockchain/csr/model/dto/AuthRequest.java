package com.blockchain.csr.model.dto;

import com.blockchain.csr.model.enums.Gender;
import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class AuthRequest {
    private String username;
    private String password;
    
    @Size(max = 50, message = "Nickname cannot exceed 50 characters")
    private String nickname;
    
    @Size(max = 50, message = "Real name cannot exceed 50 characters")
    private String realName;
    
    private Gender gender;
} 