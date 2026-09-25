package com.crustercrew.project.procurementjavabe.exception.baseException;

/**
 * Entity tidak ditemukan berdasarkan ID/criteria yang diberikan.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
