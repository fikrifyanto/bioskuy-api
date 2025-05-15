package com.bioskuy.api.dto;

import com.bioskuy.api.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date expiresAt;

    /**
     * Constructor with token, user, and expiration date
     * 
     * @param token JWT token
     * @param user User details
     * @param expiresAt Token expiration date
     */
    public JwtResponse(String token, User user, Date expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
    }
}
