package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.api.dto.CategoryDto;
import com.github.aliwocha.taskmanager.entity.Category;
import com.github.aliwocha.taskmanager.exception.DuplicateCategoryNameException;
import com.github.aliwocha.taskmanager.mapper.CategoryMapper;
import com.github.aliwocha.taskmanager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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

    public CategoryDto addCategory(CategoryDto category) {
        Optional<Category> categoryByName = categoryRepository.findByNameIgnoreCase(category.getName());
        if(categoryByName.isPresent()) {
            throw new DuplicateCategoryNameException();
        }
        Category categoryEntity = CategoryMapper.toEntity(category);
        Category savedCategory = categoryRepository.save(categoryEntity);
        return CategoryMapper.toDto(savedCategory);
    }
}
