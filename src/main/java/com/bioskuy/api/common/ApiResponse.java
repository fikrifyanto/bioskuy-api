package com.bioskuy.api.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Standard API response wrapper for all endpoints.
 * This class ensures that all API responses include a 'message' field
 * and wrap the actual response data under a 'data' field.
 */
@Getter
@Setter
public class ApiResponse<T> {
    private String message;
    private T data;

    /**
     * Constructor with message and data
     * 
     * @param message Response message
     * @param data Response data
     */
    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}