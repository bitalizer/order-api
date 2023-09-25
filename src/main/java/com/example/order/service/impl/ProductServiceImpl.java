package com.example.order.service.impl;

import com.example.order.domain.dto.request.ProductRequest;
import com.example.order.domain.dto.response.ProductResponse;
import com.example.order.domain.model.Product;
import com.example.order.repository.ProductRepository;
import com.example.order.service.ProductService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

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

	public List<Product> getProductsById(Set<Long> productIds) {
		List<Product> products = productRepository.findAllById(productIds);

		Set<Long> existingProductIds = products.stream().map(Product::getId).collect(Collectors.toSet());

		Set<Long> missingProductIds = new HashSet<>(productIds);
		missingProductIds.removeAll(existingProductIds);

		if (!missingProductIds.isEmpty()) {
			String errorMessage = String.format("Products with ids '%s' do not exist", missingProductIds);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
		}

		return products;
	}

}
