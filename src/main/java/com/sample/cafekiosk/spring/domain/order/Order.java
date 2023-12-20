package com.sample.cafekiosk.spring.domain.order;

import static com.sample.cafekiosk.spring.domain.order.OrderStatus.*;

import com.sample.cafekiosk.spring.domain.BaseEntity;
import com.sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import com.sample.cafekiosk.spring.domain.product.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime registeredDateTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    private Order(List<Product> products, LocalDateTime registeredDateTime,
        OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime = registeredDateTime;
        this.orderProducts = products.stream()
            .map(product -> new OrderProduct(this, product))
            .collect(Collectors.toList());
    }

    public static Order create(List<Product> products, LocalDateTime registeredDatetime) {
        return Order.builder()
            .orderStatus(INIT)
            .products(products)
            .registeredDateTime(registeredDatetime)
            .build();
    }

    private int calculateTotalPrice(List<Product> products) {
        return products.stream()
            .mapToInt(Product::getPrice)
            .sum();
    }
}
