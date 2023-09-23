package com.example.order.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.order.config.SecurityConfiguration;
import com.example.order.domain.dto.request.CustomerRequest;
import com.example.order.domain.dto.response.CustomerResponse;
import com.example.order.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Import(CustomerController.class)
@ContextConfiguration(classes = { SecurityConfiguration.class })
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerService customerService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testCreateCustomer() throws Exception {

		CustomerRequest customerRequest = new CustomerRequest();
		customerRequest.setFullName("Test Customer");
		customerRequest.setEmail("valid-mail@example.com");
		customerRequest.setRegistrationCode("ABC123");
		customerRequest.setPhoneNumber("+372 12345678");

		CustomerResponse customerResponse = new CustomerResponse();
		customerResponse.setId(1L);
		customerResponse.setFullName(customerRequest.getFullName());
		customerResponse.setEmail(customerRequest.getEmail());
		customerResponse.setRegistrationCode(customerRequest.getRegistrationCode());
		customerResponse.setPhoneNumber(customerRequest.getPhoneNumber());

		when(customerService.createCustomer(Mockito.any())).thenReturn(customerResponse);

		String jsonRequest = objectMapper.writeValueAsString(customerRequest);

		mockMvc
			.perform(MockMvcRequestBuilders.post("/api/v1/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(MockMvcResultMatchers.status().isCreated());

		verify(customerService, times(1)).createCustomer(Mockito.any());

	}

	@Test
	void testCreateCustomer_InvalidEmail() throws Exception {

		CustomerRequest customerRequest = new CustomerRequest();
		customerRequest.setFullName("Test Customer");
		customerRequest.setEmail("invalid-email");
		customerRequest.setRegistrationCode("ABC123");
		customerRequest.setPhoneNumber("+372 12345678");

		CustomerResponse customerResponse = new CustomerResponse();
		customerResponse.setId(1L);
		customerResponse.setFullName(customerRequest.getFullName());
		customerResponse.setEmail(customerRequest.getEmail());
		customerResponse.setRegistrationCode(customerRequest.getRegistrationCode());
		customerResponse.setPhoneNumber(customerRequest.getPhoneNumber());

		String jsonRequest = objectMapper.writeValueAsString(customerRequest);

		mockMvc
			.perform(MockMvcRequestBuilders.post("/api/v1/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(MockMvcResultMatchers.status().isBadRequest());

		verify(customerService, times(0)).createCustomer(Mockito.any());

	}

}
