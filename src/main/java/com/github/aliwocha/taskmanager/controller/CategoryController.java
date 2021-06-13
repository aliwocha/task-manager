package com.github.aliwocha.taskmanager.controller;

import com.github.aliwocha.taskmanager.dto.CategoryDto;
import com.github.aliwocha.taskmanager.dto.TaskDto;
import com.github.aliwocha.taskmanager.exception.general.IdForbiddenException;
import com.github.aliwocha.taskmanager.exception.general.IdNotMatchingException;
import com.github.aliwocha.taskmanager.service.impl.CategoryServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "Get all category names")
    @GetMapping("/names")
    public ResponseEntity<List<String>> getNames() {
        return ResponseEntity.ok(categoryService.getNames());
    }

    @ApiOperation(value = "Get category name by id")
    @GetMapping("/{id}")
    public ResponseEntity<String> getCategoryName(@PathVariable Long id) {
        return categoryService.getCategoryName(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Get tasks by category id")
    @GetMapping("/{categoryId}/tasks")
    public ResponseEntity<List<TaskDto>> getCategoryTasks(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryTasks(categoryId));
    }

    @ApiOperation(value = "Add category", notes = "Category id must not be provided.")
    @PostMapping
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

    @ApiOperation(value = "Update category")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto category, @PathVariable Long id) {
        if (!id.equals(category.getId())) {
            throw new IdNotMatchingException();
        }

        return ResponseEntity.ok(categoryService.updateCategory(category));
    }

    @ApiOperation(value = "Delete category by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
