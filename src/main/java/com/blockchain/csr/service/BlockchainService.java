package com.blockchain.csr.service;

import com.blockchain.csr.model.dto.DonationDetailDTO;

/**
 * 区块链服务接口
 * 用于处理捐赠活动的区块链操作
 */
public interface BlockchainService {
    
    /**
     * 为捐赠活动创建区块链交易
     *
     * @param userId            用户ID
     * @param donationDetailDTO
     * @return 区块链交易ID (chain ID)
     */
    String createDonationTransaction(Integer userId, DonationDetailDTO donationDetailDTO);
    
    /**
     * 验证区块链交易
     * 
     * @param chainId 区块链交易ID
     * @return 如果交易有效返回true，否则返回false
     */
    boolean verifyTransaction(String chainId);
} 