package com.blockchain.csr.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

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

    @Size(max = 2000, message = "Detail image length cannot exceed 2000 characters")
    private String detailImage;
    
    /**
     * Custom validation to ensure endTime is after startTime
     */
    public boolean isEndTimeAfterStartTime() {
        if (startTime == null || endTime == null) {
            return true; // Let @NotNull handle null validation
        }
        return endTime.isAfter(startTime);
    }
} 