package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.api.dto.CategoryDto;
import com.github.aliwocha.taskmanager.api.dto.TaskDto;
import com.github.aliwocha.taskmanager.entity.Category;
import com.github.aliwocha.taskmanager.exception.DuplicateCategoryException;
import com.github.aliwocha.taskmanager.exception.ResourceNotFoundException;
import com.github.aliwocha.taskmanager.mapper.CategoryMapper;
import com.github.aliwocha.taskmanager.mapper.TaskMapper;
import com.github.aliwocha.taskmanager.repository.CategoryRepository;
import com.github.aliwocha.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    private TaskRepository taskRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
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
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto addCategory(CategoryDto category) {
        Optional<Category> categoryByName = categoryRepository.findByNameIgnoreCase(category.getName());
        if(categoryByName.isPresent()) {
            throw new DuplicateCategoryException();
        }
        return mapAndSaveCategory(category);
    }

    public CategoryDto updateCategory(CategoryDto category) {
        Optional<Category> categoryByName = categoryRepository.findByNameIgnoreCase(category.getName());
        categoryByName.ifPresent(c -> {
            if(!c.getName().equals(category.getName())) {
                throw new DuplicateCategoryException();
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
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        if(category.get().getName().equals("No category")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This category cannot be deleted");
        }

        updateTasksBeforeDelete(id);
        categoryRepository.deleteById(id);
    }

    private void updateTasksBeforeDelete(Long id) {
        categoryRepository.findByNameIgnoreCase("No category")
                .ifPresent(c -> taskRepository.findAllByCategory_Id(id)
                        .forEach(task -> {
                            task.setCategory(c);
                            taskRepository.save(task);
                        }));
    }
}
