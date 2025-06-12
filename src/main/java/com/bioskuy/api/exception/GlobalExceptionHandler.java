package com.bioskuy.api.exception;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.helper.StringFormatHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * Handles IllegalArgumentException - thrown when a method receives
     * an argument that is inappropriate or invalid.
     * This typically indicates a client-side error where input data does not
     * meet the expected criteria or business rules.
     *
     * @param ex The IllegalArgumentException that was thrown
     * @return A ResponseEntity with a 400 Bad Request status and an error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        ApiResponse<Object> apiResponse = ResponseUtil.error(
                "Invalid input: " + ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation errors thrown by @Valid annotation.
     * It collects all field errors and returns them in a structured
     * ApiResponse format with a 400 Bad Request status.
     *
     * @param ex The exception thrown when validation fails.
     * @return A ResponseEntity containing the ApiResponse with validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = StringFormatHelper.capitalizeFirstLetter(error.getDefaultMessage());
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>("Validation error occurred", errors);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}