package com.bioskuy.api.common;

import lombok.*;

/**
 * Standard API response wrapper for all endpoints.
 * This class ensures that all API responses include a 'message' field
 * and wrap the actual response data under a 'data' field.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    private String message;

    private T data;
}