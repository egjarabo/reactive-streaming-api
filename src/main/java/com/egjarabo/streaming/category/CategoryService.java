package com.egjarabo.streaming.category;

import com.egjarabo.streaming.category.dto.CategoryRequest;
import com.egjarabo.streaming.category.dto.CategoryResponse;
import com.egjarabo.streaming.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Returns all categories
    public Flux<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .map(CategoryResponse::from);
    }

    // Returns a category by id or throws ResourceNotFoundException
    public Mono<CategoryResponse> findById(Long id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Category", id)))
                .map(CategoryResponse::from);
    }

    // Creates a new category if name does not exist
    public Mono<CategoryResponse> create(CategoryRequest request) {
        return categoryRepository.existsByName(request.name())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException(
                                "Category already exists with name: " + request.name()));
                    }
                    Category category = Category.builder()
                            .name(request.name())
                            .description(request.description())
                            .build();
                    return categoryRepository.save(category);
                })
                .map(CategoryResponse::from);
    }

    // Deletes a category by id or throws ResourceNotFoundException
    public Mono<Void> delete(Long id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Category", id)))
                .flatMap(category -> categoryRepository.delete(category));
    }
}