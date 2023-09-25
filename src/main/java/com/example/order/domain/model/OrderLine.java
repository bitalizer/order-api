package com.example.order.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "order_line")
public class OrderLine extends BaseEntityAudit {

	@Column(nullable = false)
	private Integer quantity;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")

	private Order order;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		OrderLine orderLine = (OrderLine) o;
		return getId() != null && Objects.equals(getId(), orderLine.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
