package com.example.order.domain.dto.request;

import com.example.order.validator.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerRequest {

	@NotBlank
	@Size(min = 4, max = 50)
	private String fullName;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@PhoneNumber
	private String phoneNumber;

	@NotBlank
	@Size(min = 6, max = 10)
	private String registrationCode;

}
