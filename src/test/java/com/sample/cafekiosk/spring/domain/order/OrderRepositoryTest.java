package com.sample.cafekiosk.spring.domain.order;

import static com.sample.cafekiosk.spring.domain.order.OrderStatus.PAYMENT_COMPLETED;
import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.sample.cafekiosk.spring.domain.product.Product;
import com.sample.cafekiosk.spring.domain.product.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("기간별 주문상태에 대한 주문리스트를 호출한다.")
    @Test
    void findOrdersBy() {
        // given
        Product product1 = createProduct("001", 3000);
        Product product2 = createProduct("002", 4000);
        Product product3 = createProduct("003", 5000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        LocalDateTime startTime = LocalDateTime.of(2023, 10, 19, 0, 0);
        LocalDateTime targetTime = LocalDateTime.of(2023, 10, 19, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 10, 20, 0, 0);

        Order order = Order.builder()
            .products(products)
            .registeredDateTime(targetTime)
            .orderStatus(PAYMENT_COMPLETED)
            .build();
        orderRepository.save(order);

        // when
        List<Order> orders = orderRepository.findOrdersBy(startTime, endTime,
            PAYMENT_COMPLETED);

        // then
        assertThat(orders).hasSize(1)
            .extracting("totalPrice", "registeredDateTime")
            .containsExactlyInAnyOrder(
                tuple(12000, targetTime)
            );
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
            .productNumber(productNumber)
            .name("아메리카노")
            .sellingStatus(SELLING)
            .price(price)
            .type(HANDMADE)
            .build();
    }
}