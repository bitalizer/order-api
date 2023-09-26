package com.example.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.order.domain.dto.request.OrderLineRequest;
import com.example.order.domain.dto.request.OrderRequest;
import com.example.order.domain.dto.response.OrderResponse;
import com.example.order.domain.model.Customer;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.Product;
import com.example.order.exception.CustomerNotFoundException;
import com.example.order.exception.ProductNotFoundException;
import com.example.order.repository.CustomerRepository;
import com.example.order.repository.OrderRepository;
import com.example.order.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OrderServiceTest {

	private static Customer customer;

	private static Product product;

	private static Order order;

	@Autowired
	private OrderService orderService;

	@MockBean
	private OrderRepository orderRepository;

	@MockBean
	private CustomerRepository customerRepository;

	@MockBean
	private ProductRepository productRepository;

	@BeforeAll
	public static void setUp() {
		Long customerId = 1L;
		Long productId = 5L;
		Long orderId = 50L;

		customer = new Customer();
		customer.setId(customerId);

		product = new Product();
		product.setId(productId);
		product.setName("Dummy Product");
		product.setSkuCode("SKU123");
		product.setUnitPrice(new BigDecimal("123.45"));

		order = new Order();
		order.setId(orderId);
		order.setCustomer(customer);
		order.addOrderLine(product, 10);
	}

	@Test
	void should_CreateOrder_When_ValidRequest() {

		Set<OrderLineRequest> orderLineRequests = new HashSet<>();
		orderLineRequests.add(new OrderLineRequest(10, product.getId()));

		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setCustomerId(customer.getId());
		orderRequest.setOrderLines(orderLineRequests);

		when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
		when(productRepository.findAllById(anySet())).thenReturn(Collections.singletonList(product));
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		OrderResponse orderResponse = orderService.createOrder(orderRequest);

		assertNotNull(orderResponse);
		assertNotNull(orderResponse.getOrderLines());
		assertEquals(customer.getId(), orderResponse.getCustomerId());
		assertEquals(orderLineRequests.size(), orderResponse.getOrderLines().size());
		assertEquals(order.getId(), orderResponse.getOrderId());

		// Verify that the methods were called the expected number of times
		verify(customerRepository, times(1)).findById(customer.getId());
		verify(productRepository, times(1)).findAllById(anySet());
		verify(orderRepository, times(1)).save(any(Order.class));
	}

	@Test
	void should_FailToCreateOrder_When_CustomerIdNotExists() {

		Long customerId = 1L;

		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setCustomerId(customerId);

		assertThrows(CustomerNotFoundException.class, () -> orderService.createOrder(orderRequest));
		verify(orderRepository, times(0)).save(any(Order.class));
	}

	@Test
	void should_FailToCreateOrder_When_OrderLineProductIdNotExists() {
		Set<OrderLineRequest> orderLines = new HashSet<>();
		orderLines.add(new OrderLineRequest(10, 1L));

		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setCustomerId(customer.getId());
		orderRequest.setOrderLines(orderLines);

		when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
		when(productRepository.findAllById(anySet())).thenReturn(Collections.emptyList());

		assertThrows(ProductNotFoundException.class, () -> orderService.createOrder(orderRequest));

		verify(orderRepository, times(0)).save(any(Order.class));
	}

}
