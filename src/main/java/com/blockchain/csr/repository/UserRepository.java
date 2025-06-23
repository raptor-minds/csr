package com.blockchain.csr.repository;

import com.blockchain.csr.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for User entity
 *
 * @author zhangrucheng on 2025/5/18
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    /**
     * Find user by username
     *
     * @param username the username
     * @return Optional<User>
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Check if user exists by username
     *
     * @param username the username
     * @return boolean
     */
    boolean existsByUsername(String username);
} 