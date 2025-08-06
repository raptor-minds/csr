package com.blockchain.csr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.blockchain.csr.repository.UserEventRepository;
import com.blockchain.csr.model.entity.UserEvent;
import com.blockchain.csr.model.dto.EventDto;
import com.blockchain.csr.model.entity.Event;
import com.blockchain.csr.repository.EventRepository;
import com.blockchain.csr.repository.UserRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangrucheng on 2025/5/19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserEventService{

    private final UserEventRepository userEventRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public List<EventDto> getEventsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        List<UserEvent> userEvents = userEventRepository.findByUserId(userId);

        List<Integer> eventIds = userEvents.stream()
                .map(UserEvent::getEventId)
                .collect(Collectors.toList());

        if (eventIds.isEmpty()) {
            return List.of();
        }

        List<Event> events = eventRepository.findAllById(eventIds);

        return events.stream()
                .map(this::convertToEventDto)
                .collect(Collectors.toList());
    }

    private EventDto convertToEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .type(event.getType())
                .startTime(event.getStartTime() != null ? event.getStartTime().format(DATE_TIME_FORMATTER) : null)
                .endTime(event.getEndTime() != null ? event.getEndTime().format(DATE_TIME_FORMATTER) : null)
                .status(event.getStatus())
                .build();
    }

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
