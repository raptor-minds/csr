package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Donation detail DTO for activity details
 * Used for template_id = 2
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DonationDetailDTO extends BasicDetailDTO {
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    private String chainId; // 新增区块链交易ID字段

    public DonationDetailDTO(String comment, @NotNull BigDecimal amount) {
        super(comment);
        this.amount = amount;
    }
} 