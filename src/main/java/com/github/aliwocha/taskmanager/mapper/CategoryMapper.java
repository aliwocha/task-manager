package com.github.aliwocha.taskmanager.mapper;

import com.github.aliwocha.taskmanager.api.dto.CategoryDto;
import com.github.aliwocha.taskmanager.entity.Category;

public class CategoryMapper {

    static CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
