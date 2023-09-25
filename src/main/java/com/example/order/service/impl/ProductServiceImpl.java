package com.example.order.service.impl;

import com.example.order.domain.dto.request.ProductRequest;
import com.example.order.domain.dto.response.ProductResponse;
import com.example.order.domain.model.Product;
import com.example.order.exception.ProductNotFoundException;
import com.example.order.repository.ProductRepository;
import com.example.order.service.ProductService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	private final ModelMapper mapper;

	@Override
	public ProductResponse createProduct(@RequestBody @Validated ProductRequest productRequest) {

		Product product = mapper.map(productRequest, Product.class);
		Product savedProduct = productRepository.save(product);

		return mapper.map(savedProduct, ProductResponse.class);
	}

	/**
	 * Retrieves a list of products by their IDs.
	 * @param productIds The set of product IDs to retrieve.
	 * @return A list of products matching the provided IDs.
	 * @throws ProductNotFoundException with a 404 Not Found status if any of the
	 * requested products are not found.
	 */
	public List<Product> getProductsById(Set<Long> productIds) {
		List<Product> products = productRepository.findAllById(productIds);

		Set<Long> existingProductIds = products.stream().map(Product::getId).collect(Collectors.toSet());

		Set<Long> missingProductIds = new HashSet<>(productIds);
		missingProductIds.removeAll(existingProductIds);

		if (!missingProductIds.isEmpty()) {
			throw new ProductNotFoundException(
					"Some products were not found. Missing product IDs: " + missingProductIds);
		}

		return products;
	}

}
