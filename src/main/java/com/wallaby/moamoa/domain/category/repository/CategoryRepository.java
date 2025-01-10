package com.wallaby.moamoa.domain.category.repository;

import com.wallaby.moamoa.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
