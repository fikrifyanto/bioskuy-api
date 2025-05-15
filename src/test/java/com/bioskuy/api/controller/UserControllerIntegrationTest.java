package com.bioskuy.api.controller;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.dto.JwtResponse;
import com.bioskuy.api.dto.LoginRequest;
import com.bioskuy.api.model.User;
import com.bioskuy.api.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        userRepository.deleteAll();

        // Create a test user
        testUser = new User();
        testUser.setName("Integration Test User");
        testUser.setEmail("integration-test@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setPhoneNumber("1234567890");

        // Save the user to the database
        testUser = userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        userRepository.deleteAll();
    }

    @Test
    void testCreateUser() throws Exception {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new-user@example.com");
        newUser.setPassword("password");
        newUser.setPhoneNumber("0987654321");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.name").value("New User"))
                .andExpect(jsonPath("$.data.email").value("new-user@example.com"))
                .andExpect(jsonPath("$.data.phoneNumber").value("0987654321"))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void testCreateUser_EmailAlreadyExists() throws Exception {
        User duplicateUser = new User();
        duplicateUser.setName("Duplicate User");
        duplicateUser.setEmail("integration-test@example.com"); // Same email as testUser
        duplicateUser.setPassword("password");
        duplicateUser.setPhoneNumber("0987654321");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid input: Email is already in use"));
    }

    @Test
    void testLogin_ValidCredentials() throws Exception {
        // Create a login request with the test user's credentials
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("integration-test@example.com");
        loginRequest.setPassword("password");

        // Perform the login request
        MvcResult result = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.token", notNullValue()))
                .andExpect(jsonPath("$.data.user.id").value(testUser.getId()))
                .andExpect(jsonPath("$.data.user.name").value("Integration Test User"))
                .andExpect(jsonPath("$.data.user.email").value("integration-test@example.com"))
                .andExpect(jsonPath("$.data.user.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.data.user.password").doesNotExist())
                .andReturn();

        // Extract the JWT token (not used in this test, but demonstrates successful token generation)
        String responseContent = result.getResponse().getContentAsString();
        // Demonstrate that we can parse the response, but we don't need to store it
        objectMapper.readValue(responseContent, new TypeReference<ApiResponse<JwtResponse>>() {});
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("integration-test@example.com");
        loginRequest.setPassword("wrong-password");

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials: Email or password is incorrect"));
    }

    /**
     * Helper method to get an authentication token for a test user
     */
    private String getAuthToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("integration-test@example.com");
        loginRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ApiResponse<JwtResponse> apiResponse = objectMapper.readValue(responseContent,
                new TypeReference<>() {
                });
        return apiResponse.getData().getToken();
    }

    @Test
    void testGetCurrentUser() throws Exception {
        // Get auth token
        String token = getAuthToken();

        // Now use the token to get the current user
        mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Current user retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(testUser.getId()))
                .andExpect(jsonPath("$.data.name").value("Integration Test User"))
                .andExpect(jsonPath("$.data.email").value("integration-test@example.com"))
                .andExpect(jsonPath("$.data.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void testUpdateUser() throws Exception {
        // Get auth token
        String token = getAuthToken();

        // Create updated user data
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("integration-test@example.com"); // Same email
        updatedUser.setPhoneNumber("9876543210");

        // Update the user
        mockMvc.perform(put("/users/" + testUser.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.id").value(testUser.getId()))
                .andExpect(jsonPath("$.data.name").value("Updated Name"))
                .andExpect(jsonPath("$.data.email").value("integration-test@example.com"))
                .andExpect(jsonPath("$.data.phoneNumber").value("9876543210"))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void testLogout() throws Exception {
        // Get auth token
        String token = getAuthToken();

        // Logout
        mockMvc.perform(post("/users/logout")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));

        // Try to access a protected endpoint with the blocklisted token
        // This should fail with 401 Unauthorized
        mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogout_NoToken() throws Exception {
        // Attempt to log out without a token
        mockMvc.perform(post("/users/logout"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No authentication token provided"));
    }
}
