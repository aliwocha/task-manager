package com.github.aliwocha.taskmanager.mapper;

import com.github.aliwocha.taskmanager.api.dto.TaskDto;
import com.github.aliwocha.taskmanager.entity.Category;
import com.github.aliwocha.taskmanager.entity.Task;
import com.github.aliwocha.taskmanager.repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskMapper {

    private final CategoryRepository categoryRepository;

    public TaskMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        if (task.getCategory() != null) {
            dto.setCategory(task.getCategory().getName());
        }
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
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
}
