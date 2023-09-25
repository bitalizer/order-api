package com.example.order.service;

import com.example.order.domain.dto.request.OrderRequest;
import com.example.order.domain.dto.response.OrderResponse;
import com.example.order.domain.model.Order;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public interface OrderService {

	OrderResponse createOrder(OrderRequest orderRequest);

	List<OrderResponse> getFilteredOrders(Specification<Order> spec);

}
