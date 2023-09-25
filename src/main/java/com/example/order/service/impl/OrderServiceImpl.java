package com.example.order.service.impl;

import com.example.order.domain.dto.request.OrderLineRequest;
import com.example.order.domain.dto.request.OrderRequest;
import com.example.order.domain.dto.response.OrderResponse;
import com.example.order.domain.model.Customer;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderLine;
import com.example.order.domain.model.Product;
import com.example.order.repository.OrderRepository;
import com.example.order.repository.ProductRepository;
import com.example.order.service.CustomerService;
import com.example.order.service.OrderService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final ProductRepository productRepository;

	private final CustomerService customerService;

	private final ModelMapper mapper;

	@Override
	public OrderResponse createOrder(OrderRequest orderRequest) {

		Customer customer = customerService.getCustomerById(orderRequest.getCustomerId()).orElseThrow(() -> {
			String errorMessage = String.format("Customer with id '%s' does not exist", orderRequest.getCustomerId());
			return new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
		});

		Order newOrder = new Order();

		Set<Long> productIds = orderRequest.getOrderLines()
			.stream()
			.map(OrderLineRequest::getProductId)
			.collect(Collectors.toSet());

		List<Product> products = productRepository.findAllById(productIds);

		Set<OrderLine> orderLines = orderRequest.getOrderLines().stream().map(orderLineRequest -> {
			Product product = products.stream()
				.filter(p -> p.getId().equals(orderLineRequest.getProductId()))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						String.format("Product with id '%s' does not exists", orderLineRequest.getProductId())));

			OrderLine orderLine = new OrderLine();
			orderLine.setProduct(product);
			orderLine.setQuantity(orderLineRequest.getQuantity());
			orderLine.setOrder(newOrder);
			return orderLine;
		}).collect(Collectors.toUnmodifiableSet());

		newOrder.setCustomer(customer);
		newOrder.setOrderLines(orderLines);

		Order savedOrder = orderRepository.save(newOrder);

		return mapper.map(savedOrder, OrderResponse.class);
	}

	@Override
	public List<OrderResponse> getFilteredOrders(Specification<Order> spec) {
		List<Order> filteredOrders = orderRepository.findAll(spec);
		return filteredOrders.stream().map(order -> mapper.map(order, OrderResponse.class)).toList();
	}

}
