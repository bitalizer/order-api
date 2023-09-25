package com.example.order.service.impl;

import com.example.order.domain.dto.request.CustomerRequest;
import com.example.order.domain.dto.response.CustomerResponse;
import com.example.order.domain.model.Customer;
import com.example.order.exception.CustomerNotFoundException;
import com.example.order.repository.CustomerRepository;
import com.example.order.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;

	private final ModelMapper mapper;

	/**
	 * Retrieves a customer by their unique identifier.
	 * @param customerId The ID of the customer to retrieve.
	 * @return The customer if found.
	 * @throws CustomerNotFoundException with a 404 Not Found status if the customer is
	 * not found.
	 */
	public Customer getCustomerById(Long customerId) {
		return customerRepository.findById(customerId)
			.orElseThrow(() -> new CustomerNotFoundException(
					String.format("Customer with ID '%s' was not found.", customerId)));
	}

	@Override
	public CustomerResponse createCustomer(CustomerRequest customerRequest) {

		if (customerRepository.existsByEmail(customerRequest.getEmail())) {
			String errorMessage = String.format("Email address '%s' already exists", customerRequest.getEmail());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
		}

		Customer customer = mapper.map(customerRequest, Customer.class);
		Customer savedCustomer = customerRepository.save(customer);

		return mapper.map(savedCustomer, CustomerResponse.class);
	}

}
