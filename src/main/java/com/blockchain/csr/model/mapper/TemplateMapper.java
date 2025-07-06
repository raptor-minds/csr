package com.blockchain.csr.model.mapper;

import com.blockchain.csr.model.dto.TemplateDto;
import com.blockchain.csr.model.entity.Template;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TemplateMapper {
    
    public TemplateDto toDto(Template template) {
        if (template == null) {
            return null;
        }
        
        return TemplateDto.builder()
                .id(template.getId())
                .name(template.getName())
                .totalTime(template.getTotalTime())
                .fileLink(template.getFileLink())
                .detail(template.getDetail())
                .build();
    }
    
    public List<TemplateDto> toDtoList(List<Template> templates) {
        if (templates == null) {
            return null;
        }
        
        return templates.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public Template toEntity(TemplateDto dto) {
        if (dto == null) {
            return null;
        }
        
        Template template = new Template();
        template.setId(dto.getId());
        template.setName(dto.getName());
        template.setTotalTime(dto.getTotalTime());
        template.setFileLink(dto.getFileLink());
        template.setDetail(dto.getDetail());
        
        return template;
    }
} 