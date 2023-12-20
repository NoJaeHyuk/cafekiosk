package com.sample.cafekiosk.spring.api.service.product;

import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static com.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.sample.cafekiosk.spring.api.service.product.ProductService;
import com.sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.sample.cafekiosk.spring.domain.product.Product;
import com.sample.cafekiosk.spring.domain.product.ProductRepository;
import com.sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 제품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1증가한 값이다.")
    @Test
    void createProduct() {
        // given
        Product product1 = createProduct("001", "아메리카노", SELLING, 4000);
        Product product2 = createProduct("002", "카페라떼", HOLD, 4500);
        Product product3 = createProduct("003", "팥빙수", STOP_SELLING, 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        ProductCreateRequest request = ProductCreateRequest.builder()
            .name("아메리카노")
            .price(5000)
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .build();

        // when
        ProductResponse product = productService.createProduct(request);

        // then
        assertThat(product)
            .extracting("productNumber","name","price")
            .contains("004", "아메리카노", 5000);
    }

    @DisplayName("상품이 하나도 없을 경우, 상품을 등록 시 상품 번호는 001이다.")
    @Test
    void createProductWhenProductInEmpty() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .name("아메리카노")
            .price(5000)
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .build();

        // when
        ProductResponse product = productService.createProduct(request);

        // then
        assertThat(product)
            .extracting("productNumber","name","price")
            .contains("001", "아메리카노", 5000);
    }

    private Product createProduct(String productNumber, String name, ProductSellingStatus selling,
        int price) {
        return Product.builder()
            .productNumber(productNumber)
            .name(name)
            .sellingStatus(selling)
            .price(price)
            .type(HANDMADE)
            .build();
    }

}