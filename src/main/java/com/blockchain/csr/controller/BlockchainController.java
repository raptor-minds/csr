package com.blockchain.csr.controller;

import com.blockchain.csr.model.dto.DonationDetailDTO;
import com.blockchain.csr.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 区块链服务测试控制器
 */
@RestController
@RequestMapping("/api/blockchain")
@RequiredArgsConstructor
@Slf4j
public class BlockchainController {

    private final BlockchainService blockchainService;

    /**
     * 测试创建捐赠交易
     */
    @PostMapping("/test/donation")
    public ResponseEntity<Map<String, Object>> testCreateDonationTransaction(
            @RequestParam Integer userId,
            @RequestParam Integer activityId,
            @RequestParam BigDecimal amount,
            @RequestParam String comment) {
        
        try {
            log.info("Testing blockchain donation transaction - userId: {}, activityId: {}, amount: {}", 
                    userId, activityId, amount);
            DonationDetailDTO donationDetailDTO = new DonationDetailDTO();
            donationDetailDTO.setAmount(amount);
            donationDetailDTO.setComment(comment);
            String chainId = blockchainService.createDonationTransaction(userId, donationDetailDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("chainId", chainId);
            response.put("message", "Blockchain transaction created successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error testing blockchain donation transaction: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 测试验证交易
     */
    @PostMapping("/test/verify")
    public ResponseEntity<Map<String, Object>> testVerifyTransaction(@RequestParam String chainId) {
        
        try {
            log.info("Testing blockchain transaction verification - chainId: {}", chainId);
            
            boolean isValid = blockchainService.verifyTransaction(chainId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("chainId", chainId);
            response.put("valid", isValid);
            response.put("message", "Transaction verification completed");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error testing blockchain transaction verification: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取区块链服务状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getBlockchainServiceStatus() {
        
        try {
            log.info("Checking blockchain service status");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("status", "available");
            response.put("message", "Blockchain service is running");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error checking blockchain service status: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("status", "unavailable");
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(503).body(response);
        }
    }
} 