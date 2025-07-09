package com.blockchain.csr.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * JPA converter for Serializable objects to handle JSON serialization/deserialization
 */
@Component
@Converter
@Slf4j
public class JsonConverter implements AttributeConverter<Serializable, String> {
    
    private final ObjectMapper objectMapper;
    
    @Autowired
    public JsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    public String convertToDatabaseColumn(Serializable attribute) {
        if (attribute == null) {
            return null;
        }
        
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
    
    @Override
    public Serializable convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Convert to LinkedHashMap which implements Serializable
            LinkedHashMap result = objectMapper.readValue(dbData, LinkedHashMap.class);
            return result;
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to object: {}", e.getMessage(), e);
            // Return null instead of throwing exception to handle corrupted data gracefully
            return null;
        }
    }
} 