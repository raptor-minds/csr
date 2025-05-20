package com.blockchain.csr.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.blockchain.csr.mapper.AttachmentMapper;
import com.blockchain.csr.model.entity.Attachment;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
public class AttachmentService{

    @Autowired
    private AttachmentMapper attachmentMapper;

    
    public int deleteByPrimaryKey(Integer id) {
        return attachmentMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Attachment record) {
        return attachmentMapper.insert(record);
    }

    
    public int insertSelective(Attachment record) {
        return attachmentMapper.insertSelective(record);
    }

    
    public Attachment selectByPrimaryKey(Integer id) {
        return attachmentMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Attachment record) {
        return attachmentMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Attachment record) {
        return attachmentMapper.updateByPrimaryKey(record);
    }

}
