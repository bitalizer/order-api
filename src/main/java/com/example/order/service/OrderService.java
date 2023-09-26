package com.example.order.service;

import com.example.order.domain.dto.request.OrderRequest;
import com.example.order.domain.dto.response.OrderResponse;
import com.example.order.domain.dto.response.ProductResponse;
import com.example.order.domain.model.Order;
import com.example.order.exception.OrderLineNotFoundException;
import com.example.order.exception.OrderNotFoundException;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public interface OrderService {

	/**
	 * Retrieves a list of filtered orders based on the provided specification.
	 * @param spec The specification used to filter orders.
	 * @return A list of order responses representing the filtered orders.
	 */
	List<OrderResponse> getFilteredOrders(Specification<Order> spec);

	/**
	 * Creates a new order based on the provided order request.
	 * @param orderRequest The order request containing customer and product details.
	 * @return An order response representing the newly created order.
	 */
	OrderResponse createOrder(OrderRequest orderRequest);

	/**
	 * Update the quantity of an order line in an existing order.
	 * @param orderId The ID of the order containing the order line.
	 * @param orderLineId The ID of the order line to update.
	 * @param newQuantity The new quantity for the order line.
	 * @return A {@link ProductResponse} representing the updated order line.
	 * @throws OrderNotFoundException If the order with the given ID is not found.
	 * @throws OrderLineNotFoundException If the order line with the given ID is not found
	 * in the order.
	 * @throws IllegalArgumentException If the new quantity is less than 1.
	 */
	OrderResponse updateOrderLineQuantity(Long orderId, Long orderLineId, Integer newQuantity);

}
