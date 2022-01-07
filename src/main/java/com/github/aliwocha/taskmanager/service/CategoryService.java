package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.dto.request.CategoryRequest;
import com.github.aliwocha.taskmanager.dto.response.CategoryResponse;
import com.github.aliwocha.taskmanager.dto.response.TaskResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<String> getNames();

    Optional<String> getCategoryName(Long id);

    List<TaskResponse> getCategoryTasks(Long categoryId);

    CategoryResponse addCategory(CategoryRequest categoryRequest);

    CategoryResponse updateCategory(CategoryRequest categoryRequest);

    void deleteCategory(Long id);
}
