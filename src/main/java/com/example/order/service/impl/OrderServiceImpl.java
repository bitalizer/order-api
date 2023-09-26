package com.example.order.service.impl;

import com.example.order.domain.dto.request.OrderLineRequest;
import com.example.order.domain.dto.request.OrderRequest;
import com.example.order.domain.dto.response.OrderResponse;
import com.example.order.domain.model.Customer;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderLine;
import com.example.order.domain.model.Product;
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

	@Override
	public OrderResponse createOrder(OrderRequest orderRequest) {

		Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());

		Order newOrder = new Order();

		Map<Long, Product> productMap = getProductMap(orderRequest.getOrderLines());
		Set<OrderLine> orderLines = orderRequest.getOrderLines()
			.stream()
			.map(orderLineRequest -> createOrderLine(orderLineRequest, productMap, newOrder))
			.collect(Collectors.toUnmodifiableSet());

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

	/**
	 * Creates an order line based on the provided order line request, product map, and
	 * new order.
	 * @param orderLineRequest The order line request.
	 * @param productMap The map of product IDs to products.
	 * @param order The new order to associate with the order line.
	 * @return The created order line.
	 */
	private OrderLine createOrderLine(OrderLineRequest orderLineRequest, Map<Long, Product> productMap, Order order) {

		Long productId = orderLineRequest.getProductId();
		Product product = productMap.get(productId);

		OrderLine orderLine = new OrderLine();
		orderLine.setProduct(product);
		orderLine.setQuantity(orderLineRequest.getQuantity());
		orderLine.setOrder(order);

		return orderLine;
	}

}
