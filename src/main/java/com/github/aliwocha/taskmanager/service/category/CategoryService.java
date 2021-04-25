package com.github.aliwocha.taskmanager.service.category;

import com.github.aliwocha.taskmanager.dto.CategoryDto;
import com.github.aliwocha.taskmanager.dto.TaskDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<String> getAllNames();

    Optional<String> getCategoryName(Long id);

    List<TaskDto> getCategoryTasks(Long categoryId);

    CategoryDto addCategory(CategoryDto category);

    CategoryDto updateCategory(CategoryDto category);

    void deleteCategory(Long id);
}
