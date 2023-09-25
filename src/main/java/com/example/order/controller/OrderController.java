package com.example.order.controller;

import com.example.order.domain.dto.request.OrderRequest;
import com.example.order.domain.dto.response.OrderResponse;
import com.example.order.domain.model.Order;
import com.example.order.service.OrderService;
import com.example.order.specifications.OrderSpecifications;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrderResponse createOrder(@RequestBody @Validated OrderRequest orderRequest) {
		return orderService.createOrder(orderRequest);
	}

	@GetMapping("/search")
	public List<OrderResponse> searchOrders(
			@RequestParam(name = "product_id", required = false) Optional<Long> productId,
			@RequestParam(name = "customer_id", required = false) Optional<Long> customerId,
			@RequestParam(name = "date_from",
					required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> dateFrom,
			@RequestParam(name = "date_to",
					required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> dateTo) {

		// @formatter:off
		List<Specification<Order>> specifications = Stream
			.of(
				productId.map(OrderSpecifications::hasProduct),
				customerId.map(OrderSpecifications::hasCustomer),
				dateFrom.map(OrderSpecifications::hasDateFrom),
				dateTo.map(OrderSpecifications::hasDateTo)
			)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.toList();
		// @formatter:on

		Specification<Order> spec = specifications.stream().reduce(Specification.where(null), Specification::and);

		return orderService.getFilteredOrders(spec);
	}

}
