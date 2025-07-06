package com.blockchain.csr.controller;

import com.blockchain.csr.model.dto.BaseResponse;
import com.blockchain.csr.model.dto.TemplateDto;
import com.blockchain.csr.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模板管理控制器
 * 
 * @author zhangrucheng on 2025/5/19
 */
@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
@Slf4j
public class TemplateController {

    private final TemplateService templateService;

    /**
     * 获取模板列表
     * 
     * @param name 模板名称搜索关键词
     * @return 模板列表
     */
    @GetMapping
    public ResponseEntity<BaseResponse<List<TemplateDto>>> getTemplates(
            @RequestParam(value = "name", required = false) String name) {
        try {
            log.info("Requesting template list with param - name: {}", name);
            
            List<TemplateDto> templateList = templateService.getTemplateList(name);
            return ResponseEntity.ok(BaseResponse.success(templateList));
        } catch (Exception e) {
            log.error("Error getting template list: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(BaseResponse.internalError("Failed to retrieve template list"));
        }
    }


} 