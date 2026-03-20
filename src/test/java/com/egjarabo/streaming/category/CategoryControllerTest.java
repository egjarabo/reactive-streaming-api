package com.egjarabo.streaming.category;

import com.egjarabo.streaming.AbstractIntegrationTest;
import com.egjarabo.streaming.category.dto.CategoryRequest;
import com.egjarabo.streaming.category.dto.CategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class CategoryControllerTest extends AbstractIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    // Clean state before each test
    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll().block();
    }

    @Test
    void shouldCreateCategorySuccessfully() {
        var request = new CategoryRequest("Electronics", "Electronic devices");

        webTestClient.post()
                .uri("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CategoryResponse.class)
                .value(response -> {
                    assert response.id() != null;
                    assert response.name().equals("Electronics");
                });
    }

    @Test
    void shouldReturnAllCategories() {
        // Create two categories first
        categoryRepository.save(Category.builder()
                .name("Electronics").description("Electronic devices").build()).block();
        categoryRepository.save(Category.builder()
                .name("Furniture").description("Home furniture").build()).block();

        webTestClient.get()
                .uri("/api/v1/categories")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CategoryResponse.class)
                .hasSize(2);
    }

    @Test
    void shouldReturnCategoryById() {
        // Create a category first
        var saved = categoryRepository.save(Category.builder()
                .name("Electronics").description("Electronic devices").build()).block();

        webTestClient.get()
                .uri("/api/v1/categories/{id}", saved.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponse.class)
                .value(response -> {
                    assert response.id().equals(saved.getId());
                    assert response.name().equals("Electronics");
                });
    }

    @Test
    void shouldReturn404WhenCategoryNotFound() {
        webTestClient.get()
                .uri("/api/v1/categories/{id}", 999L)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturn400WhenNameIsBlank() {
        var request = new CategoryRequest("", "Description");

        webTestClient.post()
                .uri("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturn409WhenCategoryAlreadyExists() {
        // Create category first
        categoryRepository.save(Category.builder()
                .name("Electronics").description("Electronic devices").build()).block();

        // Try to create the same category again
        var request = new CategoryRequest("Electronics", "Duplicate");

        webTestClient.post()
                .uri("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    void shouldDeleteCategorySuccessfully() {
        var saved = categoryRepository.save(Category.builder()
                .name("Electronics").description("Electronic devices").build()).block();

        webTestClient.delete()
                .uri("/api/v1/categories/{id}", saved.getId())
                .exchange()
                .expectStatus().isNoContent();
    }
}