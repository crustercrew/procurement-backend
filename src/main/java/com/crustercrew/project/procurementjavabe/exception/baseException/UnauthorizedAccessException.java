package com.crustercrew.project.procurementjavabe.exception.baseException;

/**
 * User tidak memiliki otorisasi untuk operasi yang diminta.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
