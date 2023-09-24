package com.example.order.service;

import com.example.order.domain.dto.request.ProductRequest;
import com.example.order.domain.dto.response.ProductResponse;

public interface ProductService {

	ProductResponse createProduct(ProductRequest productRequest);

}
