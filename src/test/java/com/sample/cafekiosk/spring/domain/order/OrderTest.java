package com.sample.cafekiosk.spring.domain.order;

import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;

import com.sample.cafekiosk.spring.domain.product.Product;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 생성 시 상품 리스트에서 주문 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        Product product1 = createProduct("001", 1000);
        Product product2 = createProduct("002", 2000);
        Product product3 = createProduct("003", 3000);

        List<Product> products = List.of(product1, product2, product3);

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(6000);
    }

    @DisplayName("주문 생성 시 주문 상태 값은 init이다.")
    @Test
    void statusInit() {
        // given
        Product product1 = createProduct("001", 1000);
        Product product2 = createProduct("002", 2000);
        Product product3 = createProduct("003", 3000);

        List<Product> products = List.of(product1, product2, product3);

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성 시 주문등록일시는 현재일시이다.")
    @Test
    void registeredDateTime() {
        LocalDateTime registeredDatetime = LocalDateTime.now();

        // given
        Product product1 = createProduct("001", 1000);
        Product product2 = createProduct("002", 2000);
        Product product3 = createProduct("003", 3000);

        List<Product> products = List.of(product1, product2, product3);

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDatetime);
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
            .productNumber(productNumber)
            .name("메뉴이름")
            .sellingStatus(SELLING)
            .price(price)
            .type(HANDMADE)
            .build();
    }

}