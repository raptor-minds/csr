package com.blockchain.csr.service.Impl;

import com.blockchain.csr.repository.UserRepository;
import com.blockchain.csr.model.entity.User;
import com.blockchain.csr.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
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
}
