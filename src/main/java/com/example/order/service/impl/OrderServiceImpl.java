package com.example.order.service.impl;

import com.example.order.domain.dto.request.OrderLineRequest;
import com.example.order.domain.dto.request.OrderRequest;
import com.example.order.domain.dto.response.OrderResponse;
import com.example.order.domain.model.Customer;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderLine;
import com.example.order.domain.model.Product;
import com.example.order.exception.CustomerNotFoundException;
import com.example.order.exception.OrderLineNotFoundException;
import com.example.order.repository.OrderRepository;
import com.example.order.service.CustomerService;
import com.example.order.service.OrderService;
import com.example.order.service.ProductService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final ProductService productService;

	private final CustomerService customerService;

	private final ModelMapper mapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OrderResponse> getFilteredOrders(Specification<Order> spec) {
		List<Order> filteredOrders = orderRepository.findAll(spec);
		return filteredOrders.stream().map(order -> mapper.map(order, OrderResponse.class)).toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderResponse createOrder(OrderRequest orderRequest) {

		Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());

		Order newOrder = new Order();

		Map<Long, Product> productMap = getProductMap(orderRequest.getOrderLines());
		orderRequest.getOrderLines()
			.forEach(orderLineRequest -> newOrder.addOrderLine(productMap.get(orderLineRequest.getProductId()),
					orderLineRequest.getQuantity()));

		newOrder.setCustomer(customer);
		Order savedOrder = orderRepository.save(newOrder);

		return mapper.map(savedOrder, OrderResponse.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderResponse updateOrderLineQuantity(Long orderId, Long orderLineId, Integer newQuantity) {

		Order order = getOrderById(orderId);

		OrderLine orderLine = order.getOrderLines()
			.stream()
			.filter(ol -> ol.getId().equals(orderLineId))
			.findFirst()
			.orElseThrow(() -> new OrderLineNotFoundException(
					String.format("Order Line with id '%d' does not exists", orderLineId)));

		if (newQuantity < 1) {
			order.removeOrderLine(orderLine);
		}
		else {
			orderLine.setQuantity(newQuantity);
		}

		orderLine.setQuantity(newQuantity);
		Order savedOrder = orderRepository.save(order);

		return mapper.map(savedOrder, OrderResponse.class);
	}

	/**
	 * Maps a set of order line requests to a map of product IDs to products.
	 * @param orderLineRequests The set of order line requests.
	 * @return A map of product IDs to products.
	 */
	private Map<Long, Product> getProductMap(Set<OrderLineRequest> orderLineRequests) {
		// Extract unique product IDs from the order line requests
		Set<Long> productIds = orderLineRequests.stream()
			.map(OrderLineRequest::getProductId)
			.collect(Collectors.toSet());

		// Retrieve the products associated with the extracted product IDs
		List<Product> products = productService.getProductsById(productIds);

		// Create a map that associates each product ID with its corresponding product
		return products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
	}

	private Order getOrderById(Long orderId) {

		return orderRepository.findById(orderId)
			.orElseThrow(
					() -> new CustomerNotFoundException(String.format("Order with ID '%s' was not found.", orderId)));
	}

}
