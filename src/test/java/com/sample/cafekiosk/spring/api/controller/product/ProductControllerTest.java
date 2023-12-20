package com.sample.cafekiosk.spring.api.controller.product;

import static com.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.sample.cafekiosk.spring.api.service.product.ProductService;
import com.sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @DisplayName("상품등록 등록한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .name("아메리카노")
            .price(5000)
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .build();

        // when then
        mockMvc.perform(
                post("/api/vi/products/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("상품등록 등록할 때 상품 타입은 필수값이다.")
    @Test
    void createProductWithoutType() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .name("아메리카노")
            .price(5000)
            .sellingStatus(SELLING)
            .build();

        // when then
        mockMvc.perform(
                post("/api/vi/products/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty())
            ;
    }

    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        // given
        List<ProductResponse> result = List.of();
        when(productService.getSellingProducts()).thenReturn(result);

        // when then
        mockMvc.perform(
                get("/api/vi/products/selling")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isArray())
            ;
    }
}