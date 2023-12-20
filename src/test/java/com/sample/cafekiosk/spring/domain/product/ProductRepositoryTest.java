package com.sample.cafekiosk.spring.domain.product;

import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.forDisplay;
import static com.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
        Product product1 = createProduct("001", "아메리카노", SELLING, 4000);
        Product product2 = createProduct("002", "카페라떼", HOLD, 4500);
        Product product3 = createProduct("003", "팥빙수", STOP_SELLING, 7000);

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
        Product product1 = createProduct("001", "아메리카노", SELLING, 4000);
        Product product2 = createProduct("002", "카페라떼", HOLD, 4500);
        Product product3 = createProduct("003", "팥빙수", STOP_SELLING, 7000);

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

    @DisplayName("상품의 마지막 상품번호를 조회한다.")
    @Test
    void findLatestByProduct() {
        // given
        String targetProductNumber = "003";

        Product product1 = createProduct("001", "아메리카노", SELLING, 4000);
        Product product2 = createProduct("002", "카페라떼", HOLD, 4500);
        Product product3 = createProduct(targetProductNumber, "팥빙수", STOP_SELLING, 7000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String latestProductNumber = productRepository.findLatestByProduct();

        // then
        assertThat(latestProductNumber).isEqualTo(targetProductNumber);
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