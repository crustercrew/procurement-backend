package com.crustercrew.auditservice.dto.response;

import com.crustercrew.auditservice.entity.AuditLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {

    private Long id;
    private String entityName;
    private Long entityId;
    private Long actorId;
    private String action;
    private Map<String, Object> previousState;
    private Map<String, Object> newState;
    private LocalDateTime createdAt;

    public static AuditLogResponse from(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .entityName(log.getEntityName())
                .entityId(log.getEntityId())
                .actorId(log.getActorId())
                .action(log.getAction())
                .previousState(log.getPreviousState())
                .newState(log.getNewState())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
