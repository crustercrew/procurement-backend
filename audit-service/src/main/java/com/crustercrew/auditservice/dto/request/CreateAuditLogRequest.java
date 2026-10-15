package com.crustercrew.auditservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class CreateAuditLogRequest {

    @NotBlank(message = "entityName diperlukan")
    private String entityName;

    @NotNull(message = "entityId diperlukan")
    private Long entityId;

    @NotNull(message = "actorId diperlukan")
    private Long actorId;

    @NotBlank(message = "action diperlukan")
    private String action;

    private Map<String, Object> previousState;

    private Map<String, Object> newState;
}
