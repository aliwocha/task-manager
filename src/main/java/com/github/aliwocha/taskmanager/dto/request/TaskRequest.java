package com.github.aliwocha.taskmanager.dto.request;

import com.github.aliwocha.taskmanager.entity.Task;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class TaskRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 250, message = "Title must have max 250 characters")
    private String title;

    @Size(max = 1000, message = "Description must have max 1000 characters")
    private String description;

    @NotNull(message = "Category id cannot be null")
    @Digits(integer = 4, fraction = 0, message = "Invalid number format")
    @Min(value = 1, message = "Category id must be greater or equal to 1")
    private Long categoryId;

    @NotNull(message = "Priority cannot be null")
    private Task.Priority priority;

    @NotNull(message = "Status cannot be null")
    private Task.Status status;

    private LocalDate deadline;

    @NotNull(message = "User id cannot be null")
    @Digits(integer = 4, fraction = 0, message = "Invalid number format")
    @Min(value = 1, message = "User id must be greater or equal to 1")
    private Long userId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Task.Priority getPriority() {
        return priority;
    }

    public void setPriority(Task.Priority priority) {
        this.priority = priority;
    }

    public Task.Status getStatus() {
        return status;
    }

    public void setStatus(Task.Status status) {
        this.status = status;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
