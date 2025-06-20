package com.bioskuy.api.service;

import com.bioskuy.api.entity.Customer;
import com.bioskuy.api.model.customer.CustomerResponse;
import com.bioskuy.api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user-related business logic.
 */
@Service
public class CustomerService implements CustomerServiceInterface{
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get user by email
     * 
     * @param email User email
     * @return User if found
     * @throws IllegalArgumentException if the user doesn't exist
     */
    public CustomerResponse getUserByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        return toCustomerResponse(customer);
    }

    /**
     * Create a new user
     * 
     * @param customer User to create
     * @return Created user
     * @throws IllegalArgumentException if a user with the same email already exists
     */
    public CustomerResponse createUser(Customer customer) {
        // Check if a user with the same email already exists
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Hash the password before saving
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        customerRepository.save(customer);

        return toCustomerResponse(customer);
    }

    /**
     * Update an existing user
     * 
     * @param id User ID
     * @param updatedCustomer Updated user data
     * @return Updated user
     * @throws IllegalArgumentException if the user doesn't exist, or if the email is already in use by another user
     */
    public CustomerResponse updateUser(Long id, Customer updatedCustomer) {
        // Check if the user exists
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the email is being changed and if it's already in use by another user
        if (!existingCustomer.getEmail().equals(updatedCustomer.getEmail()) &&
                customerRepository.findByEmail(updatedCustomer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Hash the password if it has been changed
        if (updatedCustomer.getPassword() != null && !updatedCustomer.getPassword().isEmpty() &&
                !updatedCustomer.getPassword().equals(existingCustomer.getPassword())) {
            updatedCustomer.setPassword(passwordEncoder.encode(updatedCustomer.getPassword()));
        } else {
            // Keep the existing password if not changed
            updatedCustomer.setPassword(existingCustomer.getPassword());
        }

        // Update the user
        updatedCustomer.setId(id);
        customerRepository.save(updatedCustomer);

        return toCustomerResponse(updatedCustomer);
    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        return response;
    }
}
