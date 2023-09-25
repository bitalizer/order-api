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

	private Map<Long, Product> getProductMap(Set<OrderLineRequest> orderLineRequests) {
		Set<Long> productIds = orderLineRequests.stream()
			.map(OrderLineRequest::getProductId)
			.collect(Collectors.toSet());

		List<Product> products = productService.getProductsById(productIds);
		return products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
	}

	private OrderLine createOrderLine(OrderLineRequest orderLineRequest, Map<Long, Product> productMap,
			Order newOrder) {

		Long productId = orderLineRequest.getProductId();
		Product product = productMap.get(productId);

		OrderLine orderLine = new OrderLine();
		orderLine.setProduct(product);
		orderLine.setQuantity(orderLineRequest.getQuantity());
		orderLine.setOrder(newOrder);

		return orderLine;
	}

}
