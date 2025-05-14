package com.bioskuy.api.dto;

import com.bioskuy.api.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for JWT response.
 * This class is used to return a JWT token along with user details after successful authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String tokenType = "Bearer";
    private User user;
    
    /**
     * Constructor with token and user
     * 
     * @param token JWT token
     * @param user User details
     */
    public JwtResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}