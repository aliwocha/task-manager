package com.github.aliwocha.taskmanager.mapper;

import com.github.aliwocha.taskmanager.api.dto.TaskDto;
import com.github.aliwocha.taskmanager.entity.Category;
import com.github.aliwocha.taskmanager.entity.Task;
import com.github.aliwocha.taskmanager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskMapper {

    private CategoryRepository categoryRepository;

    @Autowired
    public TaskMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public static TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        if(task.getCategory() != null) {
            dto.setCategory(task.getCategory().getName());
        }
        dto.setPriority(task.getPriority());
        if(task.getDeadline() != null) {
            updateStatus(task, dto);
        } else {
            dto.setStatus(task.getStatus());
        }
        dto.setDeadline(task.getDeadline());
        return dto;
    }

    public Task toEntity(TaskDto task) {
        Task entity = new Task();
        entity.setId(task.getId());
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        Optional<Category> category = categoryRepository.findByNameIgnoreCase(task.getCategory());
        category.ifPresent(entity::setCategory);
        entity.setPriority(task.getPriority());
        entity.setStatus(task.getStatus());
        entity.setDeadline(task.getDeadline());
        return entity;
    }

    private static void updateStatus(Task task, TaskDto dto) {
        if(task.isOverdue() && task.getStatus() != Task.Status.COMPLETED) {
            dto.setStatus(Task.Status.OVERDUE);
        } else {
            dto.setStatus(task.getStatus());
        }
    }
}
