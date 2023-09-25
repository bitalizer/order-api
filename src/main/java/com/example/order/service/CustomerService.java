package com.example.order.service;

import com.example.order.domain.dto.request.CustomerRequest;
import com.example.order.domain.dto.response.CustomerResponse;
import com.example.order.domain.model.Customer;

public interface CustomerService {

	/**
	 * Retrieves a customer by their unique identifier.
	 * @param customerId The ID of the customer to retrieve.
	 * @return The customer if found.
	 */
	Customer getCustomerById(Long customerId);

	/**
	 * Creates a new customer.
	 * @param customerRequest The customer request containing customer details.
	 * @return A response containing the newly created customer.
	 */
	CustomerResponse createCustomer(CustomerRequest customerRequest);

}
