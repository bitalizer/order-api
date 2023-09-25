package com.example.order.service;

import com.example.order.domain.dto.request.CustomerRequest;
import com.example.order.domain.dto.response.CustomerResponse;
import com.example.order.domain.model.Customer;
import java.util.Optional;

public interface CustomerService {

	Optional<Customer> getCustomerById(Long customerId);

	CustomerResponse createCustomer(CustomerRequest customerRequest);

}
