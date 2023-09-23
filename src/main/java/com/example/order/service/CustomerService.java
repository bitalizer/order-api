package com.example.order.service;

import com.example.order.domain.dto.request.CustomerRequest;
import com.example.order.domain.dto.response.CustomerResponse;

public interface CustomerService {

	CustomerResponse createCustomer(CustomerRequest customerRequest);

}
