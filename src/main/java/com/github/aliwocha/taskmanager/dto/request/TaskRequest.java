package com.github.aliwocha.taskmanager.dto.request;

import com.github.aliwocha.taskmanager.entity.Task;

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

    @NotBlank(message = "Category is mandatory")
    @Size(max = 50, message = "Category must have max 50 characters")
    private String category;

    @NotNull(message = "Priority cannot be null")
    @Size(max = 50, message = "Priority must have max 50 characters")
    private Task.Priority priority;

    @NotNull(message = "Status cannot be null")
    @Size(max = 50, message = "Status must have max 50 characters")
    private Task.Status status;

    @Size(max = 50, message = "Deadline must have max 50 characters")
    private LocalDate deadline;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
