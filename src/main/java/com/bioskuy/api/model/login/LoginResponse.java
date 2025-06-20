package com.bioskuy.api.model.login;

import com.bioskuy.api.model.customer.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object for JWT response.
 * This class is used to return a JWT token along with user details after successful authentication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;

    @Builder.Default
    private String tokenType = "Bearer";

    private CustomerResponse customer;

    private Date expiresAt;
}
