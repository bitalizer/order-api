package com.example.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

import com.example.order.domain.dto.request.OrderLineRequest;
import com.example.order.domain.dto.request.OrderRequest;
import com.example.order.domain.dto.response.OrderResponse;
import com.example.order.domain.model.Customer;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.Product;
import com.example.order.repository.OrderRepository;
import com.example.order.service.impl.OrderServiceImpl;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductService productService;

	@Mock
	private CustomerService customerService;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private OrderServiceImpl orderService;

	@Test
	void should_CreateOrder_When_ValidRequest() {

		Long customerId = 1L;
		Long orderId = 50L;

		Set<OrderLineRequest> orderLines = new HashSet<>();
		orderLines.add(new OrderLineRequest(10, 1L));
		Customer customer = new Customer();
		customer.setId(customerId);
		Product product = new Product();
		product.setId(1L);

		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setCustomerId(customerId);
		orderRequest.setOrderLines(orderLines);

		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setCustomerId(customerId);
		orderResponse.setOrderId(orderId);

		when(customerService.getCustomerById(customerId)).thenReturn(customer);
		when(productService.getProductsById(anySet())).thenReturn(Collections.singletonList(product));
		when(orderRepository.save(any(Order.class))).thenReturn(new Order());
		when(modelMapper.map(any(), any())).thenReturn(orderResponse);

		OrderResponse orderResponseActual = orderService.createOrder(orderRequest);

		assertNotNull(orderResponse);
		assertEquals(customerId, orderResponseActual.getCustomerId());
		assertEquals(orderId, orderResponseActual.getOrderId());
	}

	@Test
	void should_FailToCreateOrder_When_CustomerIdNotExists() {

		Long customerId = 1L;

		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setCustomerId(customerId);

		when(customerService.getCustomerById(customerId))
			.thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer not found"));

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			orderService.createOrder(orderRequest);
		});

		assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
	}

	@Test
	void should_FailToCreateOrder_When_OrderLineProductIdNotExists() {
		Long customerId = 1L;
		Set<OrderLineRequest> orderLines = new HashSet<>();
		orderLines.add(new OrderLineRequest(10, 1L)); // Product ID that does not exist
		Customer customer = new Customer();
		customer.setId(customerId);

		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setCustomerId(customerId);
		orderRequest.setOrderLines(orderLines);

		when(customerService.getCustomerById(customerId)).thenReturn(customer);
		when(productService.getProductsById(anySet()))
			.thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found"));

		assertThrows(ResponseStatusException.class, () -> {
			orderService.createOrder(orderRequest);
		});
	}

}
