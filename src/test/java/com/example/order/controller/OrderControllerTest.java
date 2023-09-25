package com.example.order.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.order.config.JacksonConfig;
import com.example.order.config.SecurityConfiguration;
import com.example.order.domain.dto.request.OrderLineRequest;
import com.example.order.domain.dto.request.OrderRequest;
import com.example.order.domain.dto.response.OrderResponse;
import com.example.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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

@Import(OrderController.class)
@ContextConfiguration(classes = { SecurityConfiguration.class, JacksonConfig.class })
@WebMvcTest(OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void should_CreateOrder_When_ValidRequest() throws Exception {

		OrderRequest productRequest = new OrderRequest();
		productRequest.setCustomerId(1L);

		Set<OrderLineRequest> orderLineRequests = new HashSet<>();
		orderLineRequests.add(new OrderLineRequest(5, 100L));
		orderLineRequests.add(new OrderLineRequest(10, 150L));

		productRequest.setOrderLines(orderLineRequests);

		when(orderService.createOrder(Mockito.any())).thenReturn(new OrderResponse());

		String jsonRequest = objectMapper.writeValueAsString(productRequest);

		mockMvc
			.perform(MockMvcRequestBuilders.post("/api/v1/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(MockMvcResultMatchers.status().isCreated());

		verify(orderService, times(1)).createOrder(Mockito.any());
	}

	@Test
	void should_FailToCreateOrder_When_CustomerIdNotSpecified() throws Exception {
		OrderRequest orderRequest = new OrderRequest();

		Set<OrderLineRequest> orderLineRequests = new HashSet<>();
		orderLineRequests.add(new OrderLineRequest(5, 100L));
		orderLineRequests.add(new OrderLineRequest(10, 150L));
		orderRequest.setOrderLines(orderLineRequests);

		when(orderService.createOrder(Mockito.any())).thenReturn(new OrderResponse());

		String jsonRequest = objectMapper.writeValueAsString(orderRequest);

		mockMvc
			.perform(MockMvcRequestBuilders.post("/api/v1/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(MockMvcResultMatchers.status().isBadRequest());

		verify(orderService, times(0)).createOrder(Mockito.any());
	}

	@Test
	void should_FailToCreateOrder_When_MissingOrderLines() throws Exception {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setCustomerId(1L);

		when(orderService.createOrder(Mockito.any())).thenReturn(new OrderResponse());

		String jsonRequest = objectMapper.writeValueAsString(orderRequest);

		mockMvc
			.perform(MockMvcRequestBuilders.post("/api/v1/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(MockMvcResultMatchers.status().isBadRequest());

		verify(orderService, times(0)).createOrder(Mockito.any());
	}

	@Test
	void should_FailToCreateOrder_When_EmptyOrderLines() throws Exception {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setCustomerId(1L);
		orderRequest.setOrderLines(Collections.emptySet());

		when(orderService.createOrder(Mockito.any())).thenReturn(new OrderResponse());

		String jsonRequest = objectMapper.writeValueAsString(orderRequest);

		mockMvc
			.perform(MockMvcRequestBuilders.post("/api/v1/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(MockMvcResultMatchers.status().isBadRequest());

		verify(orderService, times(0)).createOrder(Mockito.any());
	}

}
