package com.example.order.domain.dto.response;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductResponse {

	private Long id;

	private String name;

	private String skuCode;

	private BigDecimal unitPrice;

}
