package com.github.aliwocha.taskmanager.dto.mapper;

import com.github.aliwocha.taskmanager.dto.request.CategoryRequest;
import com.github.aliwocha.taskmanager.dto.response.CategoryResponse;
import com.github.aliwocha.taskmanager.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public static CategoryResponse toDto(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setCategoryName(category.getName());

        return categoryResponse;
    }

    public static Category toEntity(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setId(categoryRequest.getId());
        category.setName(categoryRequest.getCategoryName());

        return category;
    }
}
