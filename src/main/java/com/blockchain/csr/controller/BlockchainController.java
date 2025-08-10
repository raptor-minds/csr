package com.blockchain.csr.controller;

import com.blockchain.csr.model.dto.DonationDetailDTO;
import com.blockchain.csr.model.entity.UserActivity;
import com.blockchain.csr.service.BlockchainService;
import com.blockchain.csr.service.UserActivityService;
import com.blockchain.csr.repository.UserActivityRepository;
import com.blockchain.csr.repository.ActivityRepository;
import com.blockchain.csr.model.entity.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
    private final UserActivityRepository userActivityRepository;
    private final ActivityRepository activityRepository;
    private final UserActivityService userActivityService;
    private final ObjectMapper objectMapper;

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
    
    /**
     * 处理没有chainId的用户活动记录，生成chainId并更新记录
     */
    @PostMapping("/process-missing-chainid")
    public ResponseEntity<Map<String, Object>> processMissingChainIdRecords() {
        try {
            log.info("Processing user activities without chainId in detail");
            
            // 查询符合条件的用户活动记录
            List<UserActivity> userActivities = userActivityRepository.findWithDetailNoChainInDetailAndTemplateNot2();
            log.info("Found {} user activities from initial query", userActivities.size());
            
            // 过滤掉detail中包含chainId的记录
            List<UserActivity> filteredActivities = userActivities.stream()
                    .filter(ua -> {
                        try {
                            // 检查detail中是否包含chainId键值对
                            Serializable detailObj = ua.getDetail();
                            if (detailObj == null) {
                                log.debug("UserActivity ID: {} has null detail", ua.getId());
                                return false;
                            }
                            
                            // 将detail对象转换为JSON字符串
                            String detailStr = objectMapper.writeValueAsString(detailObj);
                            log.debug("UserActivity ID: {}, raw detail: {}", ua.getId(), detailStr);
                            
                            // 尝试解析detail为Map
                            Map<String, Object> detailMap = objectMapper.readValue(detailStr, new TypeReference<Map<String, Object>>() {});
                            boolean containsChainId = detailMap.containsKey("chainId") && detailMap.get("chainId") != null;
                            log.debug("UserActivity ID: {}, contains chainId in detail: {}", ua.getId(), containsChainId);
                            return !containsChainId;
                        } catch (Exception e) {
                            log.warn("Error parsing detail for user activity ID: {}: {}", ua.getId(), e.getMessage());
                            // 如果解析失败，假设它不包含有效的chainId
                            return true;
                        }
                    })
                    .toList();
            
            log.info("Found {} user activities after filtering out those with chainId in detail", filteredActivities.size());
            
            int processedCount = 0;
            // 循环处理每条记录
            for (UserActivity userActivity : filteredActivities) {
                try {
                    log.debug("Processing user activity ID: {}", userActivity.getId());
                    
                    // 获取对应的活动信息
                    Activity activity = activityRepository.findById(userActivity.getActivityId()).orElse(null);
                    if (activity == null) {
                        log.warn("Activity not found for user activity ID: {}", userActivity.getId());
                        continue;
                    }
                    
                    // 如果template_id等于2则跳过
                    if (activity.getTemplateId() != null && activity.getTemplateId() == 2) {
                        log.debug("Skipping user activity ID: {} as activity template_id is 2", userActivity.getId());
                        continue;
                    }
                    
                    // 解析detail获取comment
                    String comment = null;
                    try {
                        Serializable detailObj = userActivity.getDetail();
                        String detailStr = objectMapper.writeValueAsString(detailObj);
                        Map<String, Object> detailMap = objectMapper.readValue(detailStr, new TypeReference<Map<String, Object>>() {});
                        if (detailMap.containsKey("comment") && detailMap.get("comment") != null) {
                            comment = detailMap.get("comment").toString();
                        }
                        log.debug("UserActivity ID: {}, extracted comment: {}", userActivity.getId(), comment);
                    } catch (Exception e) {
                        log.warn("Error parsing detail for comment extraction for user activity ID: {}: {}", userActivity.getId(), e.getMessage());
                    }
                    
                    // 如果没有comment字段则跳过
                    if (comment == null) {
                        log.info("Skipping user activity ID: {} as it does not contain comment field", userActivity.getId());
                        continue;
                    }
                    
                    // 创建捐赠详情DTO，将duration作为amount
                    DonationDetailDTO donationDetailDTO = new DonationDetailDTO();
                    // 检查detail中是否已经有amount字段，如果有则使用它，否则使用duration
                    BigDecimal amount = null;
                    try {
                        Serializable detailObj = userActivity.getDetail();
                        String detailStr = objectMapper.writeValueAsString(detailObj);
                        Map<String, Object> detailMap = objectMapper.readValue(detailStr, new TypeReference<Map<String, Object>>() {});
                        if (detailMap.containsKey("amount") && detailMap.get("amount") != null) {
                            Object amountObj = detailMap.get("amount");
                            if (amountObj instanceof Number) {
                                amount = BigDecimal.valueOf(((Number) amountObj).doubleValue());
                            } else {
                                amount = new BigDecimal(amountObj.toString());
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Error parsing amount from detail for user activity ID: {}: {}", userActivity.getId(), e.getMessage());
                    }
                    
                    // 如果detail中没有amount，则使用duration
                    if (amount == null) {
                        amount = BigDecimal.valueOf(activity.getDuration());
                        log.debug("Using duration as amount for user activity ID: {}, amount: {}", userActivity.getId(), amount);
                    }
                    
                    donationDetailDTO.setAmount(amount);
                    donationDetailDTO.setComment(comment);
                    
                    // 调用区块链服务生成chainId
                    String chainId = blockchainService.createDonationTransaction(
                            userActivity.getUserId(), 
                            donationDetailDTO);
                    
                    // 更新detail，将chainId加入detail中
                    try {
                        Serializable detailObj = userActivity.getDetail();
                        String detailStr = objectMapper.writeValueAsString(detailObj);
                        Map<String, Object> detailMap = objectMapper.readValue(detailStr, new TypeReference<Map<String, Object>>() {});
                        detailMap.put("chainId", chainId);
                        
                        // 更新userActivity记录
//                        userActivity.setChainId(chainId);
                        userActivity.setDetail((java.io.Serializable) detailMap);
                        userActivityRepository.save(userActivity);
                        
                        processedCount++;
                        log.info("Processed user activity ID: {} with chainId: {}", userActivity.getId(), chainId);
                    } catch (Exception e) {
                        log.error("Error updating user activity detail for ID: {}: {}", userActivity.getId(), e.getMessage(), e);
                    }
                } catch (Exception e) {
                    log.error("Error processing user activity ID: {}: {}", userActivity.getId(), e.getMessage(), e);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("processedCount", processedCount);
            response.put("initialCount", userActivities.size());
            response.put("filteredCount", filteredActivities.size());
            response.put("message", "Successfully processed " + processedCount + " records");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing user activities without chainId: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}