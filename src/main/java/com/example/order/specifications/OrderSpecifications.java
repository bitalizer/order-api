package com.example.order.specifications;

import com.example.order.domain.model.Order;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSpecifications {

	public static Specification<Order> hasProduct(Long productId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder
			.equal(root.join("orderLines").join("product").get("id"), productId);
	}

	public static Specification<Order> hasCustomer(Long customerId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customer").get("id"), customerId);
	}

	public static Specification<Order> hasDateFrom(LocalDate dateFrom) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), dateFrom);
	}

	public static Specification<Order> hasDateTo(LocalDate dateTo) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), dateTo);
	}

}