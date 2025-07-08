package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.blockchain.csr.model.entity.UserActivity;
import com.blockchain.csr.repository.UserActivityRepository;
import com.blockchain.csr.model.dto.UserActivityDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserActivityService{

    private final UserActivityRepository userActivityRepository;

    public int deleteByPrimaryKey(Integer id) {
        userActivityRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(UserActivity record) {
        userActivityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(UserActivity record) {
        userActivityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public UserActivity selectByPrimaryKey(Integer id) {
        return userActivityRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(UserActivity record) {
        userActivityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(UserActivity record) {
        userActivityRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }
}
