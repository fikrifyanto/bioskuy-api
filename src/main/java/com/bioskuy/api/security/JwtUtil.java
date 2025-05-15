package com.bioskuy.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for JWT token operations.
 * Handles token generation, validation, and claim extraction.
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // Token validity duration in milliseconds (24 hours)
    private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

    // Map to store blocklisted tokens with their expiration dates
    private final Map<String, Date> blacklistedTokens = new ConcurrentHashMap<>();

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    @PostConstruct
    protected void init() {
        // Convert the base64 encoded string secret into a secure Key
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extract email from a token
     * 
     * @param token JWT token
     * @return email
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from token
     * 
     * @param token JWT token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a specific claim from a token
     * 
     * @param token JWT token
     * @param claimsResolver function to extract a specific claim
     * @return the claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from a token
     * 
     * @param token JWT token
     * @return all claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if the token is expired
     * 
     * @param token JWT token
     * @return true if the token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generate token for user
     * 
     * @param userDetails user details
     * @return JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Create a token with claims and subject
     * 
     * @param claims token claims
     * @param subject token subject (email)
     * @return JWT token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + JWT_TOKEN_VALIDITY);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setId(java.util.UUID.randomUUID().toString()) // JWT ID for uniqueness
                .signWith(key)
                .compact();
    }

    /**
     * Get token expiration date
     * 
     * @param token JWT token
     * @return expiration date
     */
    public Date getExpirationDate(String token) {
        return extractExpiration(token);
    }

    /**
     * Validate token
     * 
     * @param token JWT token
     * @param userDetails user details
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenBlacklisted(token));
    }

    /**
     * Check if a token is blocklisted
     * 
     * @param token JWT token
     * @return true if the token is blocklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    /**
     * Blacklist a token
     * 
     * @param token JWT token to blacklist
     */
    public void blacklistToken(String token) {
        try {
            Date expiry = extractExpiration(token);
            blacklistedTokens.put(token, expiry);
            logger.info("Token blacklisted successfully. Expires at: {}", expiry);
        } catch (Exception e) {
            logger.error("Error blacklisting token", e);
        }
    }

    /**
     * Scheduled task to clean up expired tokens from the blacklist
     * Runs every hour
     */
    @Scheduled(fixedRate = 3600000) // Run every hour
    public void cleanupExpiredTokens() {
        logger.info("Starting cleanup of expired blacklisted tokens");
        Date now = new Date();

        // Remove expired tokens
        Set<String> expiredTokens = blacklistedTokens.entrySet().stream()
                .filter(entry -> entry.getValue().before(now))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        expiredTokens.forEach(blacklistedTokens::remove);

        logger.info("Blacklisted tokens cleanup completed. Removed {} expired tokens. Current size: {}", 
                expiredTokens.size(), blacklistedTokens.size());
    }
}
