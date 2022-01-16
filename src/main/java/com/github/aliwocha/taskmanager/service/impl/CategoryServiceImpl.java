package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.dto.mapper.CategoryMapper;
import com.github.aliwocha.taskmanager.dto.mapper.TaskMapper;
import com.github.aliwocha.taskmanager.dto.request.CategoryRequest;
import com.github.aliwocha.taskmanager.dto.response.CategoryResponse;
import com.github.aliwocha.taskmanager.dto.response.TaskResponse;
import com.github.aliwocha.taskmanager.entity.Category;
import com.github.aliwocha.taskmanager.exception.category.CategoryForbiddenException;
import com.github.aliwocha.taskmanager.exception.category.CategoryNotFoundException;
import com.github.aliwocha.taskmanager.exception.category.DuplicateCategoryException;
import com.github.aliwocha.taskmanager.repository.CategoryRepository;
import com.github.aliwocha.taskmanager.repository.TaskRepository;
import com.github.aliwocha.taskmanager.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    private static final String DEFAULT_CATEGORY_NAME = "No category";

    public CategoryServiceImpl(CategoryRepository categoryRepository, TaskRepository taskRepository, TaskMapper taskMapper) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<String> getNames() {
        return categoryRepository.findAll()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<String> getCategoryName(Long id) {
        return categoryRepository.findById(id).map(Category::getName);
    }

    @Override
    public List<TaskResponse> getCategoryTasks(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(Category::getTasks)
                .orElseThrow(CategoryNotFoundException::new)
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        checkIfCategoryNotDuplicated(categoryRequest);
        return mapAndSaveCategory(categoryRequest);
    }

    private void checkIfCategoryNotDuplicated(CategoryRequest categoryRequest) {
        Optional<Category> categoryByName = categoryRepository.findByNameIgnoreCase(categoryRequest.getCategoryName());
        if (categoryByName.isPresent()) {
            throw new DuplicateCategoryException();
        }
    }

    private CategoryResponse mapAndSaveCategory(CategoryRequest categoryRequest) {
        Category category = CategoryMapper.toEntity(categoryRequest);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        checkIfCategoryNotDuplicated(categoryRequest, id);
        checkIfDefaultCategoryName(category);

        return updateAndSaveCategory(category, categoryRequest);
    }

    private void checkIfCategoryNotDuplicated(CategoryRequest categoryRequest, Long id) {
        Optional<Category> categoryByName = categoryRepository.findByNameIgnoreCase(categoryRequest.getCategoryName());
        categoryByName.ifPresent(category -> {
            if (!category.getId().equals(id)) {
                throw new DuplicateCategoryException();
            }
        });
    }

    private CategoryResponse updateAndSaveCategory(Category category, CategoryRequest categoryRequest) {
        category.setName(categoryRequest.getCategoryName());
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        checkIfDefaultCategoryName(category);

        updateTasksBeforeDelete(id);
        categoryRepository.deleteById(id);
    }

    private void checkIfDefaultCategoryName(Category category) {
        if (category.getName().equals(DEFAULT_CATEGORY_NAME)) {
            throw new CategoryForbiddenException("This category cannot be updated");
        }
    }

    private void updateTasksBeforeDelete(Long id) {
        Category defaultCategory = categoryRepository.findByNameIgnoreCase(DEFAULT_CATEGORY_NAME)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Default category 'No category' not found. Create first a default category with given name"));

        taskRepository.findAllByCategory_Id(id)
                .forEach(task -> task.setCategory(defaultCategory));
    }
}
