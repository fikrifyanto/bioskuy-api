package com.bioskuy.api.security;

import com.bioskuy.api.entity.Customer;
import com.bioskuy.api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of UserDetailsService to load user-specific data.
 * It is used by the DaoAuthenticationProvider to load details about the user during authentication.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Autowired
    public UserDetailsServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Load user by username (email in our case)
     * 
     * @param email the username (email) identifying the user
     * @return UserDetails object containing user details
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        // Create a UserDetails object with the user's email as username and password
        return new CustomUserDetails(customer);
    }
}