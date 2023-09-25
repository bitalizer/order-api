package com.example.order.service;

import com.example.order.domain.dto.request.OrderRequest;
import com.example.order.domain.dto.response.OrderResponse;
import com.example.order.domain.model.Order;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public interface OrderService {

	/**
	 * Creates a new order based on the provided order request.
	 * @param orderRequest The order request containing customer and product details.
	 * @return An order response representing the newly created order.
	 */
	OrderResponse createOrder(OrderRequest orderRequest);

	/**
	 * Retrieves a list of filtered orders based on the provided specification.
	 * @param spec The specification used to filter orders.
	 * @return A list of order responses representing the filtered orders.
	 */
	List<OrderResponse> getFilteredOrders(Specification<Order> spec);

}
