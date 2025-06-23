package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.blockchain.csr.model.entity.UserRoleMap;
import com.blockchain.csr.repository.UserRoleMapRepository;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserRoleMapService{

    private final UserRoleMapRepository userRoleMapRepository;

    public int deleteByPrimaryKey(Integer id) {
        userRoleMapRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(UserRoleMap record) {
        userRoleMapRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(UserRoleMap record) {
        userRoleMapRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public UserRoleMap selectByPrimaryKey(Integer id) {
        return userRoleMapRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(UserRoleMap record) {
        userRoleMapRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(UserRoleMap record) {
        userRoleMapRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

}
