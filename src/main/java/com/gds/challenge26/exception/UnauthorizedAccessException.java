package com.gds.challenge26.exception;

/**
 * Thrown when an unauthorized action is attempted.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
