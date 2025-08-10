package com.blockchain.csr.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
public class DurationDetailDTO extends BasicDetailDTO {
    
    @NotNull(message = "Duration is required")
    private Integer duration;
    private String chainId; // 区块链交易ID字段

    public DurationDetailDTO(String comment, @NotNull Integer duration, String chainId) {
        super(comment);
        this.duration = duration;
        this.chainId = chainId;
    }
}