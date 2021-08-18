package com.vladveretilnyk.clinic.repository;

import com.vladveretilnyk.clinic.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
