package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.blockchain.csr.repository.UserEventRepository;
import com.blockchain.csr.model.entity.UserEvent;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserEventService{

    private final UserEventRepository userEventRepository;

    public int deleteByPrimaryKey(Integer id) {
        userEventRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(UserEvent record) {
        userEventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(UserEvent record) {
        userEventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public UserEvent selectByPrimaryKey(Integer id) {
        return userEventRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(UserEvent record) {
        userEventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(UserEvent record) {
        userEventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

}
