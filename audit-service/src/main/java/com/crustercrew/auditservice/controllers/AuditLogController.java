package com.crustercrew.auditservice.controllers;

import com.crustercrew.auditservice.dto.response.AuditLogResponse;
import com.crustercrew.auditservice.service.AuditLogService;
import com.crustercrew.dto.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<APIResponse<Page<AuditLogResponse>>> queryAuditLogs(
            @RequestParam(required = false) String entityName,
            @RequestParam(required = false) Long entityId,
            @RequestParam(required = false) Long actorId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan audit log",
                        auditLogService.query(entityName, entityId, actorId, pageable))
        );
    }

    @GetMapping("/{entityName}/{entityId}")
    public ResponseEntity<APIResponse<Page<AuditLogResponse>>> getEntityHistory(
            @PathVariable String entityName,
            @PathVariable Long entityId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan riwayat audit",
                        auditLogService.history(entityName, entityId, pageable))
        );
    }
}
