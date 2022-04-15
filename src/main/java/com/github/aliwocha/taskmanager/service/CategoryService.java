package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.dto.request.CategoryRequest;
import com.github.aliwocha.taskmanager.dto.response.CategoryResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryResponse> getCategories();

    Optional<CategoryResponse> getCategory(Long id);

    CategoryResponse addCategory(CategoryRequest categoryRequest);

    CategoryResponse updateCategory(CategoryRequest categoryRequest, Long id);

    void deleteCategory(Long id);
}
