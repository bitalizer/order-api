package com.example.order.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.order.domain.dto.request.ProductRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductRequestValidationTest {

	private static Validator validator;

	@BeforeAll
	public static void setUp() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			validator = factory.getValidator();
		}
	}

	@ParameterizedTest
	@CsvSource({ "Sample Product, 0", "ABC, 1", "ThisNameIsWayTooLongAndExceedsTheMaximumAllowedLength, 1" })
	void validateName(String name, int expectedViolationsCount) {
		ProductRequest productRequest = new ProductRequest();
		productRequest.setName(name);
		Set<ConstraintViolation<ProductRequest>> violations = validator.validateProperty(productRequest, "name");
		assertEquals(expectedViolationsCount, violations.size());
	}

	@ParameterizedTest
	@CsvSource({ "SKU123, 0", ", 1" })
	void validateSkuCode(String skuCode, int expectedViolationsCount) {
		ProductRequest productRequest = new ProductRequest();
		productRequest.setSkuCode(skuCode);
		Set<ConstraintViolation<ProductRequest>> violations = validator.validateProperty(productRequest, "skuCode");
		assertEquals(expectedViolationsCount, violations.size());
	}

	@ParameterizedTest
	@CsvSource({ "123.45, 0", "-123.45, 1", "0, 1", "123.456, 1" })
	void validateUnitPrice(BigDecimal unitPrice, int expectedViolationsCount) {
		ProductRequest productRequest = new ProductRequest();
		productRequest.setUnitPrice(unitPrice);
		Set<ConstraintViolation<ProductRequest>> violations = validator.validateProperty(productRequest, "unitPrice");
		assertEquals(expectedViolationsCount, violations.size());
	}

}