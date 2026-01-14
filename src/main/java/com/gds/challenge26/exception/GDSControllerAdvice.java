package com.gds.challenge26.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


    /**
     * Global exception handler for all REST controllers.
     * Handles validation, business logic, batch, and generic exceptions
     * for UserController, SessionController, BatchController, and any future controllers.
     */
    @RestControllerAdvice
    public class GDSControllerAdvice {

        /**
         * Handle invalid input or bad requests (HTTP 400)
         */
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        /**
         * Handle unauthorized access (HTTP 401)
         */
        @ExceptionHandler(UnauthorizedAccessException.class)
        public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedAccessException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }

        /**
         * Handle all other exceptions (HTTP 500)
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }

        /**
         * Helper method to build a standard error response
         */
        private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
            ErrorResponse error = new ErrorResponse(status.value(), message);
            return new ResponseEntity<>(error, status);
        }
    }
