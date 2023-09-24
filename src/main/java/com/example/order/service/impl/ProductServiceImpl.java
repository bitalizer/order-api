package com.example.order.service.impl;

import com.example.order.domain.dto.request.ProductRequest;
import com.example.order.domain.dto.response.ProductResponse;
import com.example.order.domain.model.Product;
import com.example.order.repository.ProductRepository;
import com.example.order.service.ProductService;
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

}
