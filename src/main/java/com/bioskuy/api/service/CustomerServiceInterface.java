package com.bioskuy.api.service;

import com.bioskuy.api.entity.Customer;
import com.bioskuy.api.model.customer.CustomerResponse;

public interface CustomerServiceInterface {
    CustomerResponse getUserByEmail(String email);

    CustomerResponse createUser(Customer customer);

    CustomerResponse updateUser(Long id, Customer updatedCustomer);
}
