package com.blockchain.csr.service.Impl;

import com.blockchain.csr.repository.UserRepository;
import com.blockchain.csr.model.entity.User;
import com.blockchain.csr.model.enums.UserRole;
import com.blockchain.csr.model.dto.UserDto;
import com.blockchain.csr.model.dto.UserListResponse;
import com.blockchain.csr.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type User service.
 *
 * @author zhangrucheng on 2025/5/18
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(String username, String password) {
        if (existsByUsername(username)) {
            throw new IllegalArgumentException("User with username '" + username + "' already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.USER); // Set default role as USER
        userRepository.save(user);
        log.info("Created new user: {} with role: {}", username, UserRole.USER);
    }

    public void createAdminUser(String username, String password) {
        if (existsByUsername(username)) {
            throw new IllegalArgumentException("User with username '" + username + "' already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.ADMIN); // Set role as ADMIN
        userRepository.save(user);
        log.info("Created new admin user: {} with role: {}", username, UserRole.ADMIN);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void resetPassword(Integer userId, String newPassword) {
        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password reset successfully for user: {} (ID: {})", user.getUsername(), userId);
    }

    public int deleteByPrimaryKey(Integer id) {
        userRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(User record) {
        userRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(User record) {
        userRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public User selectByPrimaryKey(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(User record) {
        userRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(User record) {
        userRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    @Override
    public UserListResponse getUserList(Integer page, Integer pageSize, String username, String sortField, String sortOrder) {
        // Set default values
        int currentPage = page != null && page > 0 ? page - 1 : 0; // Convert to 0-based index
        int size = pageSize != null && pageSize > 0 ? pageSize : 10;
        
        // Create sort
        Sort sort = Sort.unsorted();
        if (StringUtils.hasText(sortField)) {
            Sort.Direction direction = "descend".equalsIgnoreCase(sortOrder) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, sortField);
        }
        
        // Create pageable
        Pageable pageable = PageRequest.of(currentPage, size, sort);
        
        // Create specification for filtering
        Specification<User> spec = createUserSpecification(username);
        
        // Get page data
        Page<User> userPage = userRepository.findAll(spec, pageable);
        
        // Convert to DTOs
        List<UserDto> userDtos = userPage.getContent().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
        
        return UserListResponse.builder()
                .data(userDtos)
                .total(userPage.getTotalElements())
                .page(page != null && page > 0 ? page : 1) // Convert back to 1-based
                .pageSize(size)
                .build();
    }

    private Specification<User> createUserSpecification(String username) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(username)) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")), 
                    "%" + username.toLowerCase() + "%"
                ));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private UserDto convertToUserDto(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        // Get event and activity counts (with null checks)
        Integer eventCount = 0;
        Integer activityCount = 0;
        try {
            eventCount = userRepository.getEventCountByUserId(user.getId());
            if (eventCount == null) eventCount = 0;
        } catch (Exception e) {
            log.warn("Failed to get event count for user {}: {}", user.getId(), e.getMessage());
        }
        
        try {
            activityCount = userRepository.getActivityCountByUserId(user.getId());
            if (activityCount == null) activityCount = 0;
        } catch (Exception e) {
            log.warn("Failed to get activity count for user {}: {}", user.getId(), e.getMessage());
        }

        // Convert role to English description
        String roleDesc = user.getRole() == UserRole.ADMIN ? "Administrator" : "User";
        
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(roleDesc)
                .location(user.getLocation())
                .reviewer(user.getReviewer())
                .createTime(user.getCreateTime() != null ? user.getCreateTime().format(formatter) : null)
                .eventCount(eventCount)
                .activityCount(activityCount)
                .build();
    }
}
