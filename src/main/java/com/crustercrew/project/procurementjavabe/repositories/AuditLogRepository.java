package com.crustercrew.project.procurementjavabe.repositories;

import com.crustercrew.project.procurementjavabe.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByEntityNameAndEntityId(String entityName,Long entityId, Pageable pageable);
    Page<AuditLog> findByEntityName(String entityName, Pageable pageable);
    Page<AuditLog> findByActorId(Long actorId, Pageable pageable);
}
