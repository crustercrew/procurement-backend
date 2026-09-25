package com.crustercrew.project.procurementjavabe.exception.baseException;

/**
 * Request tidak valid secara bisnis (bukan validasi field biasa).
 */
public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) {
        super(message);
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
