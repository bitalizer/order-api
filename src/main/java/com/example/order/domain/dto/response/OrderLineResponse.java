package com.example.order.domain.dto.response;

import lombok.Data;

@Data
public class OrderLineResponse {

	private Long id;

	private Integer quantity;

	private ProductResponse product;

}