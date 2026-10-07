package com.crustercrew.exception;

import com.crustercrew.dto.APIResponse;
import com.crustercrew.exception.baseException.BusinessValidationException;
import com.crustercrew.exception.baseException.InsufficientBudgetException;
import com.crustercrew.exception.baseException.InvalidStateTransitionException;
import com.crustercrew.exception.baseException.ResourceNotFoundException;
import com.crustercrew.exception.baseException.UnauthorizedAccessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage() != null ? ex.getMessage() : "Resource not found", request);
    }

    @ExceptionHandler(InsufficientBudgetException.class)
    public ResponseEntity<APIResponse<Void>> handleInsufficientBudget(InsufficientBudgetException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage() != null ? ex.getMessage() : "Insufficient budget", request);
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<APIResponse<Void>> handleInvalidState(InvalidStateTransitionException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage() != null ? ex.getMessage() : "Invalid state transition", request);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<APIResponse<Void>> handleUnauthorized(UnauthorizedAccessException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage() != null ? ex.getMessage() : "Access denied", request);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<APIResponse<Void>> handleBusinessValidation(BusinessValidationException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage() != null ? ex.getMessage() : "Validation failed", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(it -> it.getField() + ": " + it.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<Void>> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage() != null ? ex.getMessage() : "Invalid argument", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse<Void>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "Role Anda tidak memiliki akses ke endpoint ini", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage() != null ? ex.getMessage() : "Internal server error", request);
    }

    private ResponseEntity<APIResponse<Void>> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        APIResponse<Void> body = APIResponse.error(
                status.value(),
                message,
                status.getReasonPhrase(),
                request != null ? request.getRequestURI() : ""
        );
        return ResponseEntity.status(status).body(body);
    }
}