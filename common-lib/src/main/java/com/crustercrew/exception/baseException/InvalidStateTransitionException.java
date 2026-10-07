package com.crustercrew.exception.baseException;

public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(String message) {
        super(message);
    }
    public InvalidStateTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}