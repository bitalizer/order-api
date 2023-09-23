package com.example.order.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnTransformer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "customers")
public class Customer extends BaseEntityAudit {

	@NotBlank
	@Size(max = 50)
	@Column(nullable = false)
	private String fullName;

	@NotBlank
	@Email
	@ColumnTransformer(write = "LOWER(?)")
	@Column(nullable = false, unique = true)
	private String email;

	@NotBlank
	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private String registrationCode;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		Customer customer = (Customer) o;
		return getId() != null && Objects.equals(getId(), customer.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}