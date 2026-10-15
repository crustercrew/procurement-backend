package com.crustercrew.exception;

import com.crustercrew.dto.APIResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Mapping for Spring Security's {@link AccessDeniedException}.
 * Registered only when spring-security-core is on the classpath (see CommonLibAutoConfiguration),
 * so services without Spring Security can still load common-lib.
 */
@RestControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse<Void>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        APIResponse<Void> body = APIResponse.error(
                HttpStatus.FORBIDDEN.value(),
                "Role Anda tidak memiliki akses ke endpoint ini",
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                request != null ? request.getRequestURI() : ""
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }
}
