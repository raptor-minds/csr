package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Basic detail DTO for activity details
 * Used for template_id = 1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicDetailDTO implements Serializable {
    
    private String comment;
} 