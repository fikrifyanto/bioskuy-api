package com.bioskuy.api.service;

import com.bioskuy.api.model.User;
import com.bioskuy.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user-related business logic.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get user by email
     * 
     * @param email User email
     * @return User if found
     * @throws IllegalArgumentException if the user doesn't exist
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    /**
     * Create a new user
     * 
     * @param user User to create
     * @return Created user
     * @throws IllegalArgumentException if a user with the same email already exists
     */
    public User createUser(User user) {
        // Check if a user with the same email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Update an existing user
     * 
     * @param id User ID
     * @param updatedUser Updated user data
     * @return Updated user
     * @throws IllegalArgumentException if the user doesn't exist, or if the email is already in use by another user
     */
    public User updateUser(Long id, User updatedUser) {
        // Check if the user exists
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the email is being changed and if it's already in use by another user
        if (!existingUser.getEmail().equals(updatedUser.getEmail()) &&
                userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Hash the password if it has been changed
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty() &&
                !updatedUser.getPassword().equals(existingUser.getPassword())) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        } else {
            // Keep the existing password if not changed
            updatedUser.setPassword(existingUser.getPassword());
        }

        // Update the user
        updatedUser.setId(id);
        return userRepository.save(updatedUser);
    }
}
