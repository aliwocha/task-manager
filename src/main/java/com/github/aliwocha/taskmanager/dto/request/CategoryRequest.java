package com.github.aliwocha.taskmanager.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CategoryRequest {

    // TODO: delete id
    private Long id;

    @NotBlank(message = "Category name is mandatory")
    @Size(max = 50, message = "Category name must have max 50 characters")
    private String categoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
