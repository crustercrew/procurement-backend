package com.crustercrew.project.procurementjavabe.exception.baseException;

/**
 * Transisi status entity tidak valid (state machine violation).
 */
public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(String message) {
        super(message);
    }

    public InvalidStateTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
