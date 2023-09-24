package com.example.order.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntityAudit {

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	private String skuCode;

	@Column(nullable = false, precision = 7, scale = 2)
	private BigDecimal unitPrice;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Product product = (Product) o;
		return getId() != null && Objects.equals(getId(), product.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
