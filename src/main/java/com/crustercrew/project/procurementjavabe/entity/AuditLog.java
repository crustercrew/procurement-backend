package com.crustercrew.project.procurementjavabe.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_name",nullable = false, length = 50)
    private String entityName;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "actor_id", nullable = false)
    private Long actorId;

    @Column(nullable = false, length = 50)
    private String action; // SUBMIT, APPROVE_MANAGER, REJECT, THREE_WAY_PASS

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "previous_state", columnDefinition = "jsonb")
    private Map<String,Object> previousState = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "new_state", columnDefinition = "jsonb")
    private Map<String, Object> newState = new HashMap<>();
}
