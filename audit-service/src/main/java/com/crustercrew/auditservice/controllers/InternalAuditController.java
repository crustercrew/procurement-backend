package com.crustercrew.auditservice.controllers;

import com.crustercrew.auditservice.dto.request.CreateAuditLogRequest;
import com.crustercrew.auditservice.dto.response.AuditLogResponse;
import com.crustercrew.auditservice.service.AuditLogService;
import com.crustercrew.dto.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Internal endpoint untuk mencatat audit log. Tidak diekspos lewat API Gateway
 * (tidak ada route ke /internal/**) — dipanggil antar-service lewat Feign.
 */
@RestController
@RequestMapping("/internal/audit-logs")
@RequiredArgsConstructor
public class InternalAuditController {

    private final AuditLogService auditLogService;

    @PostMapping
    public ResponseEntity<APIResponse<AuditLogResponse>> createAuditLog(
            @Valid @RequestBody CreateAuditLogRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                APIResponse.success(201, "Audit log berhasil dicatat", auditLogService.record(request))
        );
    }
}
