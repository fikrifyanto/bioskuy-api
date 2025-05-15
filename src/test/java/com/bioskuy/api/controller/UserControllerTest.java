package com.bioskuy.api.controller;

import com.bioskuy.api.dto.LoginRequest;
import com.bioskuy.api.model.User;
import com.bioskuy.api.security.JwtUtil;
import com.bioskuy.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setPhoneNumber("1234567890");

        // Create and configure a message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        // Create and configure exception resolver
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionResolver.setMessageConverters(List.of(converter));
        exceptionResolver.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setMessageConverters(converter)
                .setHandlerExceptionResolvers(exceptionResolver)
                .build();
    }

    private void setupMockSecurityContext() {
        UserDetails userDetails = mock(UserDetails.class);
        lenient().when(userDetails.getUsername()).thenReturn("test@example.com");

        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUser_ReturnsUser() throws Exception {
        setupMockSecurityContext();
        when(userService.getUserByEmail("test@example.com")).thenReturn(testUser);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Current user retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test User"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void getCurrentUser_UserNotFound_ReturnsNotFound() throws Exception {
        setupMockSecurityContext();
        when(userService.getUserByEmail("test@example.com"))
                .thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Current user not found: User not found"));
    }

    @Test
    void createUser_ValidUser_ReturnsCreated() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test User"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void createUser_EmailAlreadyExists_ReturnsBadRequest() throws Exception {
        when(userService.createUser(any(User.class)))
                .thenThrow(new IllegalArgumentException("Email is already in use"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid input: Email is already in use"));
    }

    @Test
    void updateUser_ValidUser_ReturnsOk() throws Exception {
        setupMockSecurityContext();
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(testUser);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test User"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void updateUser_UserNotFound_ReturnsNotFound() throws Exception {
        setupMockSecurityContext();
        when(userService.updateUser(anyLong(), any(User.class)))
                .thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void updateUser_EmailAlreadyInUse_ReturnsBadRequest() throws Exception {
        setupMockSecurityContext();
        when(userService.updateUser(anyLong(), any(User.class)))
                .thenThrow(new IllegalArgumentException("Email is already in use"));

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid input: Email is already in use"));
    }

    @Test
    void login_ValidCredentials_ReturnsToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("test-jwt-token");
        when(userService.getUserByEmail(anyString())).thenReturn(testUser);

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.token").value("test-jwt-token"))
                .andExpect(jsonPath("$.data.user.id").value(1))
                .andExpect(jsonPath("$.data.user.name").value("Test User"))
                .andExpect(jsonPath("$.data.user.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.user.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.data.user.password").doesNotExist());
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrong-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials: Email or password is incorrect"));
    }

    @Test
    void logout_ValidToken_ReturnsOk() throws Exception {
        String token = "test-jwt-token";

        mockMvc.perform(post("/users/logout")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));

        // Verify that the token was blocklisted
        verify(jwtUtil).blacklistToken(token);
    }

    @Test
    void logout_NoToken_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/users/logout"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No authentication token provided"));
    }
}
