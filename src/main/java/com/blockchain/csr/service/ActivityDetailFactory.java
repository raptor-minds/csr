package com.blockchain.csr.service;

import com.blockchain.csr.model.dto.BasicDetailDTO;
import com.blockchain.csr.model.dto.DonationDetailDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Factory for creating ActivityDetailBase objects based on template ID
 */
@Component
@AllArgsConstructor
@Slf4j
public class ActivityDetailFactory {

    private final ObjectMapper objectMapper;
    
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