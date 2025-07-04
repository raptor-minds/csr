package com.blockchain.csr.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.*;

/**
 * 用于活动请求的数据传输对象
 */
@Data
public class ActivityRequestDto {
    public interface CreateGroup {}
    public interface UpdateGroup {}

    @NotBlank(message = "名称不能为空", groups = {CreateGroup.class})
    @Size(max = 45, message = "名称长度不能超过45个字符", groups = {CreateGroup.class, UpdateGroup.class})
    private String name;

    @NotNull(message = "事件ID必须提供", groups = {CreateGroup.class})
    private Integer eventId;

    @NotNull(message = "模板ID必须提供", groups = {CreateGroup.class})
    private Integer templateId;

    @Null(message = "活动持续时间", groups = {CreateGroup.class})
    private Integer duration = 0;

    @NotBlank(message = "图标不能为空", groups = {CreateGroup.class})
    @Size(max = 45, message = "图标长度不能超过45个字符", groups = {CreateGroup.class, UpdateGroup.class})
    private String icon;

    @NotBlank(message = "描述不能为空", groups = {CreateGroup.class})
    @Size(max = 1000, message = "描述长度不能超过1000个字符", groups = {CreateGroup.class, UpdateGroup.class})
    private String description;

    @NotNull(message = "开始时间必须提供", groups = {CreateGroup.class})
    private LocalDateTime startTime;

    @NotNull(message = "结束时间必须提供", groups = {CreateGroup.class})
    private LocalDateTime endTime;

    @NotBlank(message = "状态不能为空", groups = {CreateGroup.class})
    @Pattern(regexp = "not_registered|registering|full|ended", message = "无效的状态值", groups = {CreateGroup.class, UpdateGroup.class})
    private String status;

    @NotNull(message = "可见地区必须提供", groups = {CreateGroup.class})
    @Size(min = 1, message = "至少选择一个可见地区", groups = {CreateGroup.class, UpdateGroup.class})
    private List<String> visibleLocations;

    @NotNull(message = "可见角色必须提供", groups = {CreateGroup.class})
    @Size(min = 1, message = "至少选择一个可见角色", groups = {CreateGroup.class, UpdateGroup.class})
    private List<String> visibleRoles;
}