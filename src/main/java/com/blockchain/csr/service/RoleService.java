package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.blockchain.csr.model.entity.Role;
import com.blockchain.csr.repository.RoleRepository;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RoleService{

    private final RoleRepository roleRepository;

    public int deleteByPrimaryKey(Integer id) {
        roleRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(Role record) {
        roleRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(Role record) {
        roleRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public Role selectByPrimaryKey(Integer id) {
        return roleRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(Role record) {
        roleRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(Role record) {
        roleRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

}
