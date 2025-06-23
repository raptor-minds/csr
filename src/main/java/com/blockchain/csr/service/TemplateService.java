package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.blockchain.csr.repository.TemplateRepository;
import com.blockchain.csr.model.entity.Template;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService{

    private final TemplateRepository templateRepository;

    public int deleteByPrimaryKey(Integer id) {
        templateRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(Template record) {
        templateRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(Template record) {
        templateRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public Template selectByPrimaryKey(Integer id) {
        return templateRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(Template record) {
        templateRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(Template record) {
        templateRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

}
