package com.bioskuy.api.common;

/**
 * Utility class for creating standardized API responses.
 * This class provides static methods to easily create ApiResponse objects
 * with consistent message formatting.
 */
public class ResponseUtil {

    /**
     * Create a success response with data and a default success message.
     *
     * @param message The custom success message
     * @return An ApiResponse object with a success message and the provided data
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(message, null);
    }

    /**
     * Create a success response with data and a custom message.
     * 
     * @param message The custom success message
     * @param data The response data
     * @param <T> The type of the response data
     * @return An ApiResponse object with the provided message and data
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    /**
     * Create an error response with a custom error message and null data.
     * 
     * @param message The error message
     * @param <T> The type of the response data
     * @return An ApiResponse object with the provided error message and null data
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null);
    }

    /**
     * Create an error response with a custom error message and optional data.
     * 
     * @param message The error message
     * @param data The response data (can be null)
     * @param <T> The type of the response data
     * @return An ApiResponse object with the provided error message and data
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(message, data);
    }
}