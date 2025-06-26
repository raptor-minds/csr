package com.blockchain.csr.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateRequest {
    @NotBlank
    @Size(max = 45)
    private String name;

    @NotNull
    private Integer totalTime;

    @NotBlank
    @Size(max = 45)
    private String icon;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    private Boolean isDisplay;

    @NotNull
    private List<String> visibleLocations;

    @NotNull
    private List<String> visibleRoles;
} 