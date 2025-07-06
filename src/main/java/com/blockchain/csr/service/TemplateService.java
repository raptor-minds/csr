package com.blockchain.csr.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.blockchain.csr.repository.TemplateRepository;
import com.blockchain.csr.model.entity.Template;
import com.blockchain.csr.model.dto.TemplateListResponse;
import com.blockchain.csr.model.dto.TemplateDto;
import com.blockchain.csr.model.mapper.TemplateMapper;

import java.util.List;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService{

    private final TemplateRepository templateRepository;
    private final TemplateMapper templateMapper;

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

    /**
     * 获取模板列表，支持搜索
     *
     * @param name 模板名称搜索关键词
     * @return List<TemplateDto>
     */
    public List<TemplateDto> getTemplateList(String name) {
        List<Template> templates;
        
        // 根据名称搜索或获取所有模板
        if (name != null && !name.trim().isEmpty()) {
            templates = templateRepository.findByNameContainingIgnoreCase(name.trim());
        } else {
            templates = templateRepository.findAll();
        }
        
        // 转换为DTO列表
        return templateMapper.toDtoList(templates);
    }



}
