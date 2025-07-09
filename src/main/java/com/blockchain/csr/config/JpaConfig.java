package com.blockchain.csr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA Configuration to ensure converters are properly registered
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.blockchain.csr.repository")
public class JpaConfig {
    
    // This class ensures that all JPA converters are properly registered
    // The JsonConverter will be automatically detected due to @Component and @Converter annotations
} 