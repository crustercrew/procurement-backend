package com.crustercrew.auditservice.service;

import com.crustercrew.auditservice.dto.request.CreateAuditLogRequest;
import com.crustercrew.auditservice.dto.response.AuditLogResponse;
import com.crustercrew.auditservice.entity.AuditLog;
import com.crustercrew.auditservice.repositories.AuditLogRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public AuditLogResponse record(CreateAuditLogRequest request) {
        AuditLog auditLog = AuditLog.builder()
                .entityName(request.getEntityName())
                .entityId(request.getEntityId())
                .actorId(request.getActorId())
                .action(request.getAction())
                .previousState(request.getPreviousState())
                .newState(request.getNewState())
                .build();

        return AuditLogResponse.from(auditLogRepository.save(auditLog));
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> query(String entityName, Long entityId, Long actorId, Pageable pageable) {
        Specification<AuditLog> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(entityName)) {
                predicates.add(cb.equal(root.get("entityName"), entityName));
            }
            if (entityId != null) {
                predicates.add(cb.equal(root.get("entityId"), entityId));
            }
            if (actorId != null) {
                predicates.add(cb.equal(root.get("actorId"), actorId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return auditLogRepository.findAll(specification, pageable).map(AuditLogResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> history(String entityName, Long entityId, Pageable pageable) {
        return auditLogRepository
                .findByEntityNameAndEntityIdOrderByCreatedAtDesc(entityName, entityId, pageable)
                .map(AuditLogResponse::from);
    }
}
