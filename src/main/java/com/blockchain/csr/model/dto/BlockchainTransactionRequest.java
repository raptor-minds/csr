package com.blockchain.csr.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * 区块链交易请求参数DTO
 * 用于封装区块链交易请求的所有参数
 */
@Data
@Builder  // 添加Lombok的@Builder注解
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockchainTransactionRequest {
    private Integer sender;
    private String recipient;
    private String endorser;
    private DonationDetailDTO transaction;
    private String uuid;

    // 构造方法
    public BlockchainTransactionRequest(Integer sender, String recipient, String endorser, 
                                       DonationDetailDTO transaction, String uuid) {
        this.sender = sender;
        this.recipient = recipient;
        this.endorser = endorser;
        this.transaction = transaction;
        this.uuid = uuid;
    }

    // 静态工厂方法
    public static BlockchainTransactionRequest of(Integer sender, String recipient, String endorser,
                                                 DonationDetailDTO transaction) {
        return new BlockchainTransactionRequest(
            sender,
            recipient,
            endorser,
            transaction,
            UUID.randomUUID().toString()
        );
    }
}