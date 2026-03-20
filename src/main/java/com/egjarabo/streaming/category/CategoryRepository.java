package com.egjarabo.streaming.category;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {

    // Check if a category with the given name already exists
    Mono<Boolean> existsByName(String name);
}