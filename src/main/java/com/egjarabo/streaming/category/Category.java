package com.egjarabo.streaming.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("category") // Maps to the category table in PostgreSQL
public class Category {

    @Id
    private Long id;

    private String name;

    private String description;
}
