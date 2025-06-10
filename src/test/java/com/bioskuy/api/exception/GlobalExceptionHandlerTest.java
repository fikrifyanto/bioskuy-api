package com.bioskuy.api.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the GlobalExceptionHandler class.
 * This class tests that 404 errors are properly handled.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test that a request to a non-existent endpoint returns a 404 status code
     * and an appropriate error message.
     */
    @Test
    public void testNonExistentEndpoint() throws Exception {
        // Send a request to a non-existent endpoint
        mockMvc.perform(get("/non-existent-endpoint"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("The requested resource was not found: non-existent-endpoint"));
    }
}