package com.blockchain.csr.service;

import com.blockchain.csr.model.dto.BasicDetailDTO;
import com.blockchain.csr.model.dto.DonationDetailDTO;
import com.blockchain.csr.model.dto.DurationDetailDTO;
import com.blockchain.csr.model.entity.Activity;
import com.blockchain.csr.repository.ActivityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * Factory for creating ActivityDetailBase objects based on template ID
 */
@Component
@AllArgsConstructor
@Slf4j
public class ActivityDetailFactory {

    private final ObjectMapper objectMapper;
    private final BlockchainService blockchainService;
    private final ActivityRepository activityRepository;
    
    /**
     * Create BasicDetailDTO from template ID and detail map (from request)
     * 
     * @param templateId the template ID
     * @param detailMap the detail data map from request
     * @return BasicDetailDTO instance
     * @throws IllegalArgumentException if validation fails or unsupported template ID
     */
    public BasicDetailDTO createDetail(Integer templateId, Map<String, Object> detailMap) {
        if (templateId == null) {
            throw new IllegalArgumentException("Template ID cannot be null");
        }
        
        if (detailMap == null || detailMap.isEmpty()) {
            throw new IllegalArgumentException("Detail map cannot be null or empty");
        }

        // Validate comment (required for all templates)
        String comment = (String) detailMap.get("comment");
        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment is required");
        }
        
        return switch (templateId) {
            case 1 -> createBasicDetail(comment);
            case 2 -> createDonationDetail(comment, detailMap);
            default -> throw new IllegalArgumentException("Unsupported template ID: " + templateId);
        };
    }
    
    /**
     * Create BasicDetailDTO from template ID and detail map with user context (for blockchain operations)
     * 
     * @param templateId the template ID
     * @param detailMap the detail data map from request
     * @param userId the user ID (for blockchain operations)
     * @param activityId the activity ID (for blockchain operations)
     * @return BasicDetailDTO instance
     * @throws IllegalArgumentException if validation fails or unsupported template ID
     */
    public BasicDetailDTO createDetail(Integer templateId, Map<String, Object> detailMap, Integer userId, Integer activityId) {
        if (templateId == null) {
            throw new IllegalArgumentException("Template ID cannot be null");
        }
        
        if (detailMap == null || detailMap.isEmpty()) {
            throw new IllegalArgumentException("Detail map cannot be null or empty");
        }

        // Validate comment (required for all templates)
        String comment = (String) detailMap.get("comment");
        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment is required");
        }

        return createDonationDetailWithBlockchain(templateId, detailMap, userId, activityId);
    }
    
    /**
     * Create BasicDetailDTO from template ID and serializable detail (from database)
     * 
     * @param templateId the template ID
     * @param detail the detail data from database
     * @return BasicDetailDTO instance
     * @throws IllegalArgumentException if validation fails or unsupported template ID
     */
    public BasicDetailDTO createDetail(Integer templateId, Serializable detail) {
        if (templateId == null) {
            throw new IllegalArgumentException("Template ID cannot be null");
        }
        
        if (detail == null) {
            return null; // Return null for null detail from database
        }

        try {
            return switch (templateId) {
                case 1 -> objectMapper.convertValue(detail, BasicDetailDTO.class);
                case 2 -> objectMapper.convertValue(detail, DonationDetailDTO.class);
                default -> throw new IllegalArgumentException("Unsupported template ID: " + templateId);
            };
        } catch (Exception e) {
            log.error("Error converting detail for template ID {}: {}", templateId, e.getMessage(), e);
            return null; // Return null if conversion fails
        }
    }
    
    /**
     * Create BasicDetailDTO for template ID 1
     */
    private BasicDetailDTO createBasicDetail(String comment) {
        return new BasicDetailDTO(comment);
    }
    
    /**
     * Create DonationDetailDTO for template ID 2
     */
    private DonationDetailDTO createDonationDetail(String comment, Map<String, Object> detailMap) {
        Object amountObj = detailMap.get("amount");
        if (amountObj == null) {
            throw new IllegalArgumentException("Amount is required for donation activities");
        }
        
        BigDecimal amount = convertToAmount(amountObj);
        if (amount.compareTo(BigDecimal.valueOf(0.01)) < 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.01");
        }

        return new DonationDetailDTO(comment, amount);
    }
    
    /**
     * Create DonationDetailDTO for template ID 2 with blockchain integration
     */
    private BasicDetailDTO createDonationDetailWithBlockchain(Integer templateId, Map<String, Object> detailMap, Integer userId, Integer activityId) {
        String comment = (String) detailMap.get("comment");
        BigDecimal amount = null;
        Integer duration = null;
        if(templateId == 2) {
            Object amountObj = detailMap.get("amount");
            if (amountObj == null) {
                throw new IllegalArgumentException("Amount is required for donation activities");
            }

          amount = convertToAmount(amountObj);
            if (amount.compareTo(BigDecimal.valueOf(0.01)) < 0) {
                throw new IllegalArgumentException("Amount must be greater than 0.01");
            }
        }else{
            Optional<Activity> activity = activityRepository.findById(activityId);
            if(activity.isPresent()) {
                duration = activity.get().getDuration();
                amount = BigDecimal.valueOf(duration);
            }
        }

        // 调用区块链服务创建交易
        try {
            log.info("Creating blockchain transaction for donation - userId: {}, activityId: {}, amount: {}", 
                    userId, activityId, amount);
            
            // 创建捐赠详情DTO，包含chainId
            DonationDetailDTO donationDetailDTO = new DonationDetailDTO(comment, amount);

            String chainId = blockchainService.createDonationTransaction(userId, donationDetailDTO);

            log.info("Successfully created blockchain transaction with chainId: {}", chainId);
            
            donationDetailDTO.setChainId(chainId); // 保存区块链交易ID到DTO

            if(templateId != 2){
                return new DurationDetailDTO(comment, duration, chainId);
            }
            
            return donationDetailDTO;
            
        } catch (Exception e) {
            log.error("Failed to create blockchain transaction for donation - userId: {}, activityId: {}, amount: {}", 
                    userId, activityId, amount, e);
            throw new RuntimeException("Failed to create blockchain transaction for donation", e);
        }
    }
    
    /**
     * Convert amount object to BigDecimal
     */
    private BigDecimal convertToAmount(Object amountObj) {
        if (amountObj instanceof String) {
            try {
                return new BigDecimal((String) amountObj);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid amount format: " + amountObj);
            }
        } else if (amountObj instanceof Number) {
            return BigDecimal.valueOf(((Number) amountObj).doubleValue());
        } else {
            throw new IllegalArgumentException("Amount must be a number or string, got: " + amountObj.getClass().getSimpleName());
        }
    }
} 