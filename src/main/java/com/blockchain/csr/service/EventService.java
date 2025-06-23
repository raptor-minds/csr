package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.blockchain.csr.model.entity.Event;
import com.blockchain.csr.repository.EventRepository;
/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class EventService{

    private final EventRepository eventRepository;

    
    public int deleteByPrimaryKey(Integer id) {
        eventRepository.deleteById(id);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insert(Event record) {
        eventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int insertSelective(Event record) {
        eventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public Event selectByPrimaryKey(Integer id) {
        return eventRepository.findById(id).orElse(null);
    }

    public int updateByPrimaryKeySelective(Event record) {
        eventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

    public int updateByPrimaryKey(Event record) {
        eventRepository.save(record);
        return 1; // JPA doesn't return affected rows, assuming success
    }

}
