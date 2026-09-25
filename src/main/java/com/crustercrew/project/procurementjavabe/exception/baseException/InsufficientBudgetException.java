package com.crustercrew.project.procurementjavabe.exception.baseException;

/**
 * Budget departemen tidak mencukupi untuk operasi yang diminta.
 */
public class InsufficientBudgetException extends RuntimeException {
    public InsufficientBudgetException(String message) {
        super(message);
    }

    public InsufficientBudgetException(String message, Throwable cause) {
        super(message, cause);
    }
}
