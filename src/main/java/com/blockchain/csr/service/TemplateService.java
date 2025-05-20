package com.blockchain.csr.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.blockchain.csr.mapper.TemplateMapper;
import com.blockchain.csr.model.entity.Template;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
public class TemplateService{

    @Autowired
    private TemplateMapper templateMapper;

    
    public int deleteByPrimaryKey(Integer id) {
        return templateMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Template record) {
        return templateMapper.insert(record);
    }

    
    public int insertSelective(Template record) {
        return templateMapper.insertSelective(record);
    }

    
    public Template selectByPrimaryKey(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Template record) {
        return templateMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Template record) {
        return templateMapper.updateByPrimaryKey(record);
    }

}
