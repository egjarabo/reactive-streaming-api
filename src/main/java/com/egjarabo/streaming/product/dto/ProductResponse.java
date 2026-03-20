package com.egjarabo.streaming.product.dto;

import com.egjarabo.streaming.product.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        Long categoryId
) {
    // Factory method — maps entity to response DTO
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategoryId()
        );
    }
}
