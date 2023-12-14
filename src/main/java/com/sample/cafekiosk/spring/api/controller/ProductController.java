package com.sample.cafekiosk.spring.api.controller;

import com.sample.cafekiosk.spring.api.service.product.ProductService;
import com.sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/products/selling")
    public List<ProductResponse> getSellingProducts() {
        return productService.getSellingProducts();
    }

}
