package com.crustercrew.project.procurementjavabe.exception;

import com.crustercrew.project.procurementjavabe.dto.GeneralErrorResponse;
import com.crustercrew.project.procurementjavabe.exception.baseException.BusinessValidationException;
import com.crustercrew.project.procurementjavabe.exception.baseException.InsufficientBudgetException;
import com.crustercrew.project.procurementjavabe.exception.baseException.InvalidStateTransitionException;
import com.crustercrew.project.procurementjavabe.exception.baseException.ResourceNotFoundException;
import com.crustercrew.project.procurementjavabe.exception.baseException.UnauthorizedAccessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(com.crustercrew.project.procurementjavabe.exception.baseException.ResourceNotFoundException.class)
    public ResponseEntity<GeneralErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage() != null ? ex.getMessage() : "Resource not found", request);
    }

    @ExceptionHandler(com.crustercrew.project.procurementjavabe.exception.baseException.InsufficientBudgetException.class)
    public ResponseEntity<GeneralErrorResponse> handleInsufficientBudget(InsufficientBudgetException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage() != null ? ex.getMessage() : "Insufficient budget", request);
    }

    @ExceptionHandler(com.crustercrew.project.procurementjavabe.exception.baseException.InvalidStateTransitionException.class)
    public ResponseEntity<GeneralErrorResponse> handleInvalidState(InvalidStateTransitionException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage() != null ? ex.getMessage() : "Invalid state transition", request);
    }

    @ExceptionHandler(com.crustercrew.project.procurementjavabe.exception.baseException.UnauthorizedAccessException.class)
    public ResponseEntity<GeneralErrorResponse> handleUnauthorized(UnauthorizedAccessException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage() != null ? ex.getMessage() : "Access denied", request);
    }

    @ExceptionHandler(com.crustercrew.project.procurementjavabe.exception.baseException.BusinessValidationException.class)
    public ResponseEntity<GeneralErrorResponse> handleBusinessValidation(BusinessValidationException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage() != null ? ex.getMessage() : "Validation failed", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(it -> it.getField() + ": " + it.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GeneralErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage() != null ? ex.getMessage() : "Invalid argument", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
    }

    private ResponseEntity<GeneralErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        GeneralErrorResponse body = new GeneralErrorResponse(
                Instant.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }
}
