package com.example.order.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class OrderRequest {

	@NotNull
	@Getter
	@Setter
	private Long customerId;

	@NotEmpty
	@Valid
	private Set<OrderLineRequest> orderLines;

	@NotNull
	public Set<OrderLineRequest> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(@NotNull Set<OrderLineRequest> orderLines) {
		this.orderLines = orderLines;
	}

}