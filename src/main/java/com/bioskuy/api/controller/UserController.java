package com.bioskuy.api.controller;

import com.bioskuy.api.common.ApiResponse;
import com.bioskuy.api.common.ResponseUtil;
import com.bioskuy.api.dto.JwtResponse;
import com.bioskuy.api.dto.LoginRequest;
import com.bioskuy.api.model.User;
import com.bioskuy.api.security.JwtUtil;
import com.bioskuy.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Controller for user-related endpoints.
 * Implements the User API as defined in the OpenAPI specification.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Get the current logged-in user
     * 
     * @return ResponseEntity with ApiResponse containing the current user
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> viewProfile() {
        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the user details (email) from the authentication object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        // Get the user from the service
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(ResponseUtil.success("Current user retrieved successfully", user.withoutPassword()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.error("Current user not found: " + e.getMessage()));
        }
    }

    /**
     * Create a new user
     * 
     * @param user User data
     * @return ResponseEntity with ApiResponse containing the created user
     */
    @PostMapping
    public ResponseEntity<ApiResponse<User>> register(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.success("User created successfully", createdUser.withoutPassword()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error("Invalid input: " + e.getMessage(), user));
        }
    }

    /**
     * Update user
     * 
     * @param id User ID
     * @param user Updated user data
     * @return ResponseEntity with ApiResponse containing the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateProfile(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(ResponseUtil.success("User updated successfully", updatedUser.withoutPassword()));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtil.error(e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtil.error("Invalid input: " + e.getMessage(), user));
            }
        }
    }

    /**
     * User login
     * 
     * @param loginRequest Login request data
     * @return ResponseEntity with ApiResponse containing the JWT token and user details
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            // Get user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generate JWT token
            String token = jwtUtil.generateToken(userDetails);

            // Get user from service
            User user = userService.getUserByEmail(loginRequest.getEmail());

            // Get token expiration date
            Date expiresAt = jwtUtil.getExpirationDate(token);

            // Create a JWT response with an expiration date
            JwtResponse jwtResponse = new JwtResponse(token, user.withoutPassword(), expiresAt);

            return ResponseEntity.ok(ResponseUtil.success("Login successful", jwtResponse));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseUtil.error("Invalid credentials: Email or password is incorrect"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error("An error occurred during login: " + e.getMessage()));
        }
    }

    /**
     * User logout
     * 
     * @param authHeader Authorization header containing the JWT token
     * @return ResponseEntity with ApiResponse indicating logout success
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Extract token from the Authorization header
            if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // Blocklist the token
                jwtUtil.blacklistToken(token);

                // Clear the security context
                SecurityContextHolder.clearContext();

                return ResponseEntity.ok(ResponseUtil.success("Logout successful"));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error("No authentication token provided"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error("An error occurred during logout: " + e.getMessage()));
        }
    }

    /**
     * Refresh JWT token
     * 
     * @param authHeader Authorization header containing the JWT token
     * @return ResponseEntity with ApiResponse containing the new JWT token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Extract token from the Authorization header
            if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // Check if the token is blocklisted
                if (jwtUtil.isTokenBlacklisted(token)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ResponseUtil.error("Invalid token: Token has been revoked"));
                }

                // Extract email from a token
                String email = jwtUtil.extractEmail(token);

                // Check if the token is expired
                if (jwtUtil.extractExpiration(token).before(new Date())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ResponseUtil.error("Token has expired"));
                }

                // Get user details
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                // Generate new token
                String newToken = jwtUtil.generateToken(userDetails);

                // Get user from service
                User user = userService.getUserByEmail(email);

                // Get token expiration date
                Date expiresAt = jwtUtil.getExpirationDate(newToken);

                // Create a JWT response with an expiration date
                JwtResponse jwtResponse = new JwtResponse(newToken, user.withoutPassword(), expiresAt);

                // Blocklist the old token
                jwtUtil.blacklistToken(token);

                return ResponseEntity.ok(ResponseUtil.success("Token refreshed successfully", jwtResponse));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error("No authentication token provided"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error("An error occurred during token refresh: " + e.getMessage()));
        }
    }
}
