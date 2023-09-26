package com.example.order.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderLineUpdateRequest {

	@NotNull
	@Min(0)
	private Integer quantity;

}
