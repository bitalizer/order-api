package com.example.order.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

@RequiredArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntityAudit {

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<OrderLine> orderLines = new HashSet<>();

	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.EAGER)
	private Customer customer;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Order order = (Order) o;
		return getId() != null && Objects.equals(getId(), order.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	public Set<OrderLine> getOrderLines() {
		return Collections.unmodifiableSet(orderLines);
	}

	public void addOrderLine(Product product, int quantity) {
		OrderLine orderLine = new OrderLine();
		orderLine.setProduct(product);
		orderLine.setQuantity(quantity);
		orderLine.setOrder(this);
		orderLines.add(orderLine);
	}

	public void removeOrderLine(OrderLine orderLine) {
		orderLines.remove(orderLine);
		orderLine.setOrder(null);
		orderLine.setProduct(null);
		orderLine.setQuantity(0);
	}

}