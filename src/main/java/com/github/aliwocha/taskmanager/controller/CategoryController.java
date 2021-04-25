package com.github.aliwocha.taskmanager.controller;

import com.github.aliwocha.taskmanager.dto.CategoryDto;
import com.github.aliwocha.taskmanager.dto.TaskDto;
import com.github.aliwocha.taskmanager.exception.IdForbiddenException;
import com.github.aliwocha.taskmanager.exception.IdNotMatchingException;
import com.github.aliwocha.taskmanager.service.category.CategoryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/names")
    public List<String> getAllNames() {
        return categoryService.getAllNames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getCategoryName(@PathVariable Long id) {
        return categoryService.getCategoryName(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{categoryId}/tasks")
    public List<TaskDto> getCategoryTasks(@PathVariable Long categoryId) {
        return categoryService.getCategoryTasks(categoryId);
    }

    @PostMapping("")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto category) {
        if (category.getId() != null) {
            throw new IdForbiddenException();
        }

        CategoryDto savedCategory = categoryService.addCategory(category);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCategory.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto category, @PathVariable Long id) {
        if (!id.equals(category.getId())) {
            throw new IdNotMatchingException();
        }

        CategoryDto updatedCategory = categoryService.updateCategory(category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
