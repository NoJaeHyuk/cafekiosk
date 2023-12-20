package com.sample.cafekiosk.spring.api.service.order;

import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.sample.cafekiosk.spring.domain.product.ProductType.BAKERY;
import static com.sample.cafekiosk.spring.domain.product.ProductType.BOTTLE;
import static com.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.sample.cafekiosk.spring.client.mail.MailSendClient;
import com.sample.cafekiosk.spring.domain.history.MailSendHistory;
import com.sample.cafekiosk.spring.domain.history.MailSendHistoryRepository;
import com.sample.cafekiosk.spring.domain.order.Order;
import com.sample.cafekiosk.spring.domain.order.OrderRepository;
import com.sample.cafekiosk.spring.domain.order.OrderStatus;
import com.sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import com.sample.cafekiosk.spring.domain.product.Product;
import com.sample.cafekiosk.spring.domain.product.ProductRepository;
import com.sample.cafekiosk.spring.domain.product.ProductType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @MockBean
    private MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        // given
        LocalDateTime targetDateTime = LocalDateTime.of(2023, 3, 5, 0, 0);

        Product product1 = createProduct("001", BOTTLE, 1000);
        Product product2 = createProduct("002", BAKERY, 2000);
        Product product3 = createProduct("003", HANDMADE, 3000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = orderCreateByStatus(LocalDateTime.of(2023, 3, 4, 23, 59, 59), products);
        Order order2 = orderCreateByStatus(targetDateTime, products);
        Order order3 = orderCreateByStatus(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products);
        Order order4 = orderCreateByStatus(LocalDateTime.of(2023, 3, 6, 0, 0), products);

        when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString())).thenReturn(true);

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 3, 5),
            "test@test.com");

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
            .extracting("content")
            .contains("총 매출 합계는 12000원 입니다.");
    }

    private Order orderCreateByStatus(LocalDateTime targetDateTime, List<Product> products) {
        Order order = Order.builder()
            .products(products)
            .registeredDateTime(targetDateTime)
            .orderStatus(OrderStatus.PAYMENT_COMPLETED)
            .build();

        return orderRepository.save(order);
    }

    private Product createProduct(String productNumber, ProductType type, int price) {
        return Product.builder()
            .productNumber(productNumber)
            .name("메뉴이름")
            .sellingStatus(SELLING)
            .price(price)
            .type(type)
            .build();
    }

}