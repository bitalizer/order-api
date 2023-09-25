package com.example.order.domain.dto.response;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderResponse {

	private Long orderId;

	private Long customerId;

	private LocalDateTime createdAt;

	private Set<OrderLineResponse> orderLines;

}
