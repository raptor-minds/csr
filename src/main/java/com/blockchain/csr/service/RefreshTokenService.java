package com.blockchain.csr.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class RefreshTokenService {
    
    private final ConcurrentMap<String, String> validRefreshTokens = new ConcurrentHashMap<>();
    
    public void addRefreshToken(String username, String refreshToken) {
        validRefreshTokens.put(refreshToken, username);
    }
    
    public boolean isValidRefreshToken(String refreshToken) {
        return validRefreshTokens.containsKey(refreshToken);
    }
    
    public String getUsernameFromRefreshToken(String refreshToken) {
        return validRefreshTokens.get(refreshToken);
    }
    
    public void removeRefreshToken(String refreshToken) {
        validRefreshTokens.remove(refreshToken);
    }
    
    public void removeAllRefreshTokensForUser(String username) {
        validRefreshTokens.entrySet().removeIf(entry -> username.equals(entry.getValue()));
    }
} 