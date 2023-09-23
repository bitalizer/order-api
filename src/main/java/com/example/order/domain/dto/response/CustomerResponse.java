package com.example.order.domain.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerResponse {

	private Long id;

	private String fullName;

	private String email;

	private String phoneNumber;

	private String registrationCode;

}
