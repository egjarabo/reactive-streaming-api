package com.egjarabo.streaming.product;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    // Find all products belonging to a specific category
    Flux<Product> findByCategoryId(Long categoryId);

    // Check if a product with the given name already exists
    Mono<Boolean> existsByName(String name);
}
