package com.example.order.service;

import com.example.order.domain.dto.request.ProductRequest;
import com.example.order.domain.dto.response.ProductResponse;
import com.example.order.domain.model.Product;
import java.util.List;
import java.util.Set;

public interface ProductService {

	ProductResponse createProduct(ProductRequest productRequest);

	List<Product> getProductsById(Set<Long> productIds);

}
