package com.blockchain.csr.controller;

import com.blockchain.csr.model.dto.FeedbackDto;
import com.blockchain.csr.model.entity.Feedback;
import com.blockchain.csr.service.FeedbackService;
import com.blockchain.csr.config.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private SecurityUtils securityUtils;

    @PostMapping("/feedback")
    public java.util.Map<String, Object> createFeedback(@RequestBody FeedbackDto dto) {
        Integer userId = securityUtils.getCurrentUserId();
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setFeedbackDetail(dto.getFeedbackDetail());
        Feedback saved = feedbackService.saveFeedback(feedback);
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("feedbackDetail", saved.getFeedbackDetail());
        result.put("message", "反馈提交成功");
        return result;
    }

    @GetMapping("/feedbacks")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FeedbackDto> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks().stream().map(fb -> {
            FeedbackDto dto = new FeedbackDto();
            dto.setId(fb.getId());
            dto.setUserId(fb.getUserId());
            dto.setFeedbackDetail(fb.getFeedbackDetail());
            return dto;
        }).collect(Collectors.toList());
    }
} 