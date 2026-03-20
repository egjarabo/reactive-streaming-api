package com.egjarabo.streaming.category.dto;

import com.egjarabo.streaming.category.Category;

public record CategoryResponse(
        Long id,
        String name,
        String description
) {
    // Factory method — maps entity to response DTO
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
