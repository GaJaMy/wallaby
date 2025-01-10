package com.wallaby.moamoa.domain.category.service;

import com.wallaby.moamoa.common.exception.CustomException;
import com.wallaby.moamoa.common.exception.ErrorCode;
import com.wallaby.moamoa.domain.category.entity.Category;
import com.wallaby.moamoa.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CustomException(ErrorCode.SUCCESS));
    }
}
