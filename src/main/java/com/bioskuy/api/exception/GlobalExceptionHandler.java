package com.bioskuy.api.exception;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler for the application.
 * This class handles exceptions thrown by the application and returns appropriate error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle NoHandlerFoundException - thrown when no handler is found for a request.
     * This typically happens when a user tries to access a non-existent endpoint.
     *
     * @param ex The NoHandlerFoundException that was thrown
     * @return A ResponseEntity with a 404 status code and an error message
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFoundException(
            NoHandlerFoundException ex) {
        ApiResponse<Object> apiResponse = ResponseUtil.error(
                "The requested resource was not found: " + ex.getRequestURL());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle NoResourceFoundException - thrown when a resource is not found.
     * This typically happens when a user tries to access a non-existent resource.
     *
     * @param ex The NoResourceFoundException that was thrown
     * @return A ResponseEntity with a 404 status code and an error message
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoResourceFoundException(
            NoResourceFoundException ex) {
        ApiResponse<Object> apiResponse = ResponseUtil.error(
                "The requested resource was not found: " + ex.getResourcePath());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle all other exceptions that are not specifically handled.
     *
     * @param ex The Exception that was thrown
     * @return A ResponseEntity with a 500 status code and an error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(
            Exception ex) {
        ApiResponse<Object> apiResponse = ResponseUtil.error(
                "An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}