package com.github.aliwocha.taskmanager.api.controller;

import com.github.aliwocha.taskmanager.api.dto.CategoryDto;
import com.github.aliwocha.taskmanager.exception.IdForbiddenException;
import com.github.aliwocha.taskmanager.exception.IdNotMatchingException;
import com.github.aliwocha.taskmanager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/names")
    public List<String> getAllNames() {
        return categoryService.getAllNames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryName(@PathVariable Long id) {
        return categoryService.getCategoryName(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto category) {
        if(category.getId() != null) {
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
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto category, @PathVariable Long id) {
        if(!id.equals(category.getId())) {
            throw new IdNotMatchingException();
        }
        CategoryDto updatedCategory = categoryService.updateCategory(category);
        return ResponseEntity.ok(updatedCategory);
    }
}
