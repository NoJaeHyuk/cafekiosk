package com.sample.cafekiosk.spring.domain.product;

import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.sample.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//@SpringBootTest
@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        Product product1 = Product.builder()
            .productNumber("001")
            .name("아메리카노")
            .sellingStatus(SELLING)
            .price(4000)
            .type(HANDMADE)
            .build();
        Product product2 = Product.builder()
            .productNumber("002")
            .name("카페라떼")
            .sellingStatus(HOLD)
            .price(4500)
            .type(HANDMADE)
            .build();
        Product product3 = Product.builder()
            .productNumber("003")
            .name("팥빙수")
            .sellingStatus(STOP_SELLING)
            .price(7000)
            .type(HANDMADE)
            .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(
            forDisplay());

        // then
        assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "아메리카노", SELLING),
                tuple("002", "카페라떼", HOLD)
            );
    }

    @DisplayName("상품번호 리스트로 상품을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given
        Product product1 = Product.builder()
            .productNumber("001")
            .name("아메리카노")
            .sellingStatus(SELLING)
            .price(4000)
            .type(HANDMADE)
            .build();
        Product product2 = Product.builder()
            .productNumber("002")
            .name("카페라떼")
            .sellingStatus(HOLD)
            .price(4500)
            .type(HANDMADE)
            .build();
        Product product3 = Product.builder()
            .productNumber("003")
            .name("팥빙수")
            .sellingStatus(STOP_SELLING)
            .price(7000)
            .type(HANDMADE)
            .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "아메리카노", SELLING),
                tuple("002", "카페라떼", HOLD)
            );
    }

}