package com.egjarabo.streaming.product;

import com.egjarabo.streaming.category.CategoryRepository;
import com.egjarabo.streaming.common.exception.ResourceNotFoundException;
import com.egjarabo.streaming.product.dto.ProductRequest;
import com.egjarabo.streaming.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private static final String PRODUCT = "Product";

    // Returns all products
    public Flux<ProductResponse> findAll() {
        return productRepository.findAll()
                .map(ProductResponse::from);
    }

    // Returns all products belonging to a category
    public Flux<ProductResponse> findByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Category", categoryId)))
                .flatMapMany(category -> productRepository.findByCategoryId(categoryId))
                .map(ProductResponse::from);
    }

    // Returns a product by id or throws ResourceNotFoundException
    public Mono<ProductResponse> findById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(PRODUCT, id)))
                .map(ProductResponse::from);
    }

    // Creates a new product after validating category exists and name is unique
    public Mono<ProductResponse> create(ProductRequest request) {
        return categoryRepository.findById(request.categoryId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Category", request.categoryId())))
                .flatMap(category -> productRepository.existsByName(request.name()))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException(
                                "Product already exists with name: " + request.name()));
                    }
                    Product product = Product.builder()
                            .name(request.name())
                            .description(request.description())
                            .price(request.price())
                            .stock(request.stock())
                            .categoryId(request.categoryId())
                            .build();
                    return productRepository.save(product);
                })
                .map(ProductResponse::from);
    }

    // Updates stock for a product
    public Mono<ProductResponse> updateStock(Long id, Integer stock) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(PRODUCT, id)))
                .flatMap(product -> {
                    product.setStock(stock);
                    return productRepository.save(product);
                })
                .map(ProductResponse::from);
    }

    // Deletes a product by id or throws ResourceNotFoundException
    public Mono<Void> delete(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(PRODUCT, id)))
                .flatMap(product -> productRepository.delete(product));
    }
}