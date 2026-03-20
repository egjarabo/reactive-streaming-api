package com.egjarabo.streaming.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("product") // Maps to the product table in PostgreSQL
public class Product {

    @Id
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    // Explicit mapping to the FK column
    private Long categoryId;
}
