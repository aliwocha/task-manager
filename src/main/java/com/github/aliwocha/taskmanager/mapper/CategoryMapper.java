package com.github.aliwocha.taskmanager.mapper;

import com.github.aliwocha.taskmanager.api.dto.CategoryDto;
import com.github.aliwocha.taskmanager.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public static CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());

        return dto;
    }

    public static Category toEntity(CategoryDto category) {
        Category entity = new Category();
        entity.setId(category.getId());
        entity.setName(category.getName());

        return entity;
    }
}
