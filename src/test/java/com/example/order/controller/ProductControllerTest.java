package com.example.order.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.order.config.JacksonConfig;
import com.example.order.config.SecurityConfiguration;
import com.example.order.domain.dto.request.ProductRequest;
import com.example.order.domain.dto.response.ProductResponse;
import com.example.order.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Import(ProductController.class)
@ContextConfiguration(classes = { SecurityConfiguration.class, JacksonConfig.class })
@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void should_CreateProduct_When_ValidRequest() throws Exception {

		ProductRequest productRequest = new ProductRequest();
		productRequest.setName("Sample Product");
		productRequest.setSkuCode("SKU123");
		productRequest.setUnitPrice(new BigDecimal("1234.56"));

		ProductResponse productResponse = new ProductResponse();
		productResponse.setId(1L);
		productResponse.setName(productRequest.getName());
		productResponse.setSkuCode(productRequest.getSkuCode());
		productResponse.setUnitPrice(productRequest.getUnitPrice());

		when(productService.createProduct(Mockito.any())).thenReturn(productResponse);

		String jsonRequest = objectMapper.writeValueAsString(productRequest);

		mockMvc
			.perform(MockMvcRequestBuilders.post("/api/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(productResponse.getId().intValue())))
			.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(productResponse.getName())))
			.andExpect(MockMvcResultMatchers.jsonPath("$.sku_code", Matchers.is(productResponse.getSkuCode())))
			.andExpect(MockMvcResultMatchers.jsonPath("$.unit_price",
					Matchers.is(productRequest.getUnitPrice().doubleValue())));

		verify(productService, times(1)).createProduct(Mockito.any());
	}

}
