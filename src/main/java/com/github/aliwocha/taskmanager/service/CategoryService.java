package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.api.dto.CategoryDto;
import com.github.aliwocha.taskmanager.api.dto.TaskDto;
import com.github.aliwocha.taskmanager.entity.Category;
import com.github.aliwocha.taskmanager.exception.CategoryForbiddenException;
import com.github.aliwocha.taskmanager.exception.DuplicateCategoryException;
import com.github.aliwocha.taskmanager.exception.ResourceNotFoundException;
import com.github.aliwocha.taskmanager.mapper.CategoryMapper;
import com.github.aliwocha.taskmanager.mapper.TaskMapper;
import com.github.aliwocha.taskmanager.repository.CategoryRepository;
import com.github.aliwocha.taskmanager.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    private final String DEFAULT_CATEGORY_NAME = "No category";

    public CategoryService(CategoryRepository categoryRepository, TaskRepository taskRepository, TaskMapper taskMapper) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public List<String> getAllNames() {
        return categoryRepository.findAll()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    public Optional<String> getCategoryName(Long id) {
        return categoryRepository.findById(id).map(Category::getName);
    }

    public List<TaskDto> getCategoryTasks(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(Category::getTasks)
                .orElseThrow(ResourceNotFoundException::new)
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto addCategory(CategoryDto category) {
        Optional<Category> categoryByName = categoryRepository.findByNameIgnoreCase(category.getName());
        if (categoryByName.isPresent()) {
            throw new DuplicateCategoryException();
        }

        return mapAndSaveCategory(category);
    }

    public CategoryDto updateCategory(CategoryDto category) {
        Optional<Category> categoryByName = categoryRepository.findByNameIgnoreCase(category.getName());
        categoryByName.ifPresent(c -> {
            if (!c.getId().equals(category.getId())) {
                throw new DuplicateCategoryException();
            }
        });

        Optional<Category> categoryById = categoryRepository.findById(category.getId());
        categoryById.ifPresent(c -> {
            if (c.getName().equals(DEFAULT_CATEGORY_NAME)) {
                throw new CategoryForbiddenException("This category cannot be updated");
            }
        });

        return mapAndSaveCategory(category);
    }

    private CategoryDto mapAndSaveCategory(CategoryDto category) {
        Category categoryEntity = CategoryMapper.toEntity(category);
        Category savedCategory = categoryRepository.save(categoryEntity);
        return CategoryMapper.toDto(savedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        if (category.getName().equals(DEFAULT_CATEGORY_NAME)) {
            throw new CategoryForbiddenException("This category cannot be deleted");
        }

        updateTasksBeforeDelete(id);
        categoryRepository.deleteById(id);
    }

    private void updateTasksBeforeDelete(Long id) {
        Category defaultCategory = categoryRepository.findByNameIgnoreCase(DEFAULT_CATEGORY_NAME)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Default category 'No category' not found. Create first a default category with given name"));

        taskRepository.findAllByCategory_Id(id)
                .forEach(task -> task.setCategory(defaultCategory));
    }
}
