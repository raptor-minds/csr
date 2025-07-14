package com.blockchain.csr.service.Impl;

import com.blockchain.csr.model.dto.DonationDetailDTO;
import com.blockchain.csr.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 区块链服务实现类
 * 负责调用第三方区块链API
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlockchainServiceImpl implements BlockchainService {

    private final RestTemplate restTemplate;

    @Value("${blockchain.api.base-url:http://localhost:8081}")
    private String blockchainApiBaseUrl;

    @Value("${blockchain.api.api-key:}")
    private String apiKey;

    @Value("${blockchain.api.timeout:5000}")
    private int timeout;

    @Override
    public String createDonationTransaction(Integer userId, DonationDetailDTO donationDetailDTO) {
        try {
            log.info("Creating blockchain transaction for donation - userId: {}, activityId: {}, amount: {}", 
                    userId, donationDetailDTO.getAmount(), donationDetailDTO.getComment());

            // 构建请求URL
            String url = blockchainApiBaseUrl + "/api/blockchain/transactions/add";

            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (apiKey != null && !apiKey.isEmpty()) {
                headers.set("X-API-Key", apiKey);
            }

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("sender", userId);
            requestBody.put("recipient", "admin");
            requestBody.put("endorser", "admin");
            requestBody.put("transaction", donationDetailDTO.toString());
            requestBody.put("uuid", UUID.randomUUID());
            requestBody.put("createdTime", System.currentTimeMillis());

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // 调用第三方区块链API
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                String txId = response.getBody();
                if (!txId.isEmpty()) {
                    log.info("Successfully created blockchain transaction with tx id: {}", txId);
                    return txId;
                } else {
                    log.error("Blockchain API returned null or empty chainId");
                    throw new RuntimeException("Failed to get chain ID from blockchain service");
                }
            } else {
                log.error("Blockchain API returned non-success status: {}", response.getStatusCode());
                throw new RuntimeException("Blockchain service returned error status: " + response.getStatusCode());
            }

        } catch (ResourceAccessException e) {
            log.error("Failed to connect to blockchain service: {}", e.getMessage(), e);
            throw new RuntimeException("Blockchain service is unavailable", e);
        } catch (Exception e) {
            log.error("Error creating blockchain transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create blockchain transaction", e);
        }
    }

    @Override
    public boolean verifyTransaction(String chainId) {
        try {
            log.info("Verifying blockchain transaction with chainId: {}", chainId);

            // 构建请求URL
            String url = blockchainApiBaseUrl + "/api/blockchain/transactions/" + chainId + "/verify";

            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (apiKey != null && !apiKey.isEmpty()) {
                headers.set("X-API-Key", apiKey);
            }

            HttpEntity<Void> request = new HttpEntity<>(headers);

            // 调用第三方区块链API验证交易
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Boolean isValid = (Boolean) responseBody.get("valid");
                
                if (isValid != null) {
                    log.info("Transaction verification result for chainId {}: {}", chainId, isValid);
                    return isValid;
                } else {
                    log.error("Blockchain API returned null verification result");
                    return false;
                }
            } else {
                log.error("Blockchain API returned non-success status for verification: {}", response.getStatusCode());
                return false;
            }

        } catch (ResourceAccessException e) {
            log.error("Failed to connect to blockchain service for verification: {}", e.getMessage(), e);
            return false;
        } catch (Exception e) {
            log.error("Error verifying blockchain transaction: {}", e.getMessage(), e);
            return false;
        }
    }
} 