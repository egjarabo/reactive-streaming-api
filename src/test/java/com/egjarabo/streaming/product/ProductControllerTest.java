package com.egjarabo.streaming.product;

import com.egjarabo.streaming.AbstractIntegrationTest;
import com.egjarabo.streaming.category.Category;
import com.egjarabo.streaming.category.CategoryRepository;
import com.egjarabo.streaming.product.dto.ProductRequest;
import com.egjarabo.streaming.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Long categoryId;

    // Clean state and create base category before each test
    @BeforeEach
    void setUp() {
        productRepository.deleteAll().block();
        categoryRepository.deleteAll().block();

        // All product tests need at least one category
        var category = categoryRepository.save(Category.builder()
                .name("Electronics")
                .description("Electronic devices")
                .build()).block();
        categoryId = category.getId();
    }

    @Test
    void shouldCreateProductSuccessfully() {
        var request = new ProductRequest(
                "Laptop Pro 15", "High performance laptop",
                new BigDecimal("1299.99"), 10, categoryId);

        webTestClient.post()
                .uri("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductResponse.class)
                .value(response -> {
                    assert response.id() != null;
                    assert response.name().equals("Laptop Pro 15");
                    assert response.categoryId().equals(categoryId);
                });
    }

    @Test
    void shouldReturnAllProducts() {
        productRepository.save(Product.builder()
                .name("Laptop").description("Laptop desc")
                .price(new BigDecimal("1299.99")).stock(10)
                .categoryId(categoryId).build()).block();
        productRepository.save(Product.builder()
                .name("Mouse").description("Mouse desc")
                .price(new BigDecimal("35.00")).stock(50)
                .categoryId(categoryId).build()).block();

        webTestClient.get()
                .uri("/api/v1/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponse.class)
                .hasSize(2);
    }

    @Test
    void shouldReturnProductById() {
        var saved = productRepository.save(Product.builder()
                .name("Laptop").description("Laptop desc")
                .price(new BigDecimal("1299.99")).stock(10)
                .categoryId(categoryId).build()).block();

        webTestClient.get()
                .uri("/api/v1/products/{id}", saved.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .value(response -> {
                    assert response.id().equals(saved.getId());
                    assert response.name().equals("Laptop");
                });
    }

    @Test
    void shouldReturnProductsByCategory() {
        productRepository.save(Product.builder()
                .name("Laptop").description("desc")
                .price(new BigDecimal("1299.99")).stock(10)
                .categoryId(categoryId).build()).block();
        productRepository.save(Product.builder()
                .name("Mouse").description("desc")
                .price(new BigDecimal("35.00")).stock(50)
                .categoryId(categoryId).build()).block();

        webTestClient.get()
                .uri("/api/v1/products/category/{categoryId}", categoryId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponse.class)
                .hasSize(2);
    }

    @Test
    void shouldReturn404WhenProductNotFound() {
        webTestClient.get()
                .uri("/api/v1/products/{id}", 999L)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturn400WhenPriceIsNegative() {
        var request = new ProductRequest(
                "Laptop", "desc",
                new BigDecimal("-100.00"), 10, categoryId);

        webTestClient.post()
                .uri("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturn404WhenCategoryDoesNotExist() {
        var request = new ProductRequest(
                "Laptop", "desc",
                new BigDecimal("1299.99"), 10, 999L);

        webTestClient.post()
                .uri("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldUpdateStockSuccessfully() {
        var saved = productRepository.save(Product.builder()
                .name("Laptop").description("desc")
                .price(new BigDecimal("1299.99")).stock(10)
                .categoryId(categoryId).build()).block();

        webTestClient.patch()
                .uri("/api/v1/products/{id}/stock?stock=25", saved.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .value(response -> assertTrue (response.stock() == 25));
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        var saved = productRepository.save(Product.builder()
                .name("Laptop").description("desc")
                .price(new BigDecimal("1299.99")).stock(10)
                .categoryId(categoryId).build()).block();

        webTestClient.delete()
                .uri("/api/v1/products/{id}", saved.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingProduct() {
        webTestClient.delete()
                .uri("/api/v1/products/{id}", 999L)
                .exchange()
                .expectStatus().isNotFound();
    }
}