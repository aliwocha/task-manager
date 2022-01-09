package com.github.aliwocha.taskmanager.dto.mapper;

import com.github.aliwocha.taskmanager.dto.request.TaskRequest;
import com.github.aliwocha.taskmanager.dto.response.TaskResponse;
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

    public TaskResponse toDto(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        if (task.getCategory() != null) {
            taskResponse.setCategory(task.getCategory().getName());
        }
        taskResponse.setPriority(task.getPriority());
        taskResponse.setStatus(task.getStatus());
        taskResponse.setDeadline(task.getDeadline());

        return taskResponse;
    }

    public Task toEntity(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        Optional<Category> category = categoryRepository.findByNameIgnoreCase(taskRequest.getCategory());
        category.ifPresent(task::setCategory);
        task.setPriority(taskRequest.getPriority());
        task.setStatus(taskRequest.getStatus());
        task.setDeadline(taskRequest.getDeadline());

        return task;
    }
}
