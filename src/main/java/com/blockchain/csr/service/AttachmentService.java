package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.blockchain.csr.repository.AttachmentRepository;
import com.blockchain.csr.model.entity.Attachment;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AttachmentService{

    private final AttachmentRepository attachmentRepository;

    public int deleteByPrimaryKey(Integer id) {
        attachmentRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(Attachment record) {
        attachmentRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(Attachment record) {
        attachmentRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public Attachment selectByPrimaryKey(Integer id) {
        return attachmentRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(Attachment record) {
        attachmentRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(Attachment record) {
        attachmentRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

}
