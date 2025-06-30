package com.blockchain.csr.model.dto;

import com.blockchain.csr.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {
    
    @Size(max = 50, message = "Nickname cannot exceed 50 characters")
    private String nickname;
    
    @Size(max = 50, message = "Real name cannot exceed 50 characters")
    private String realName;
    
    private Gender gender;
} 