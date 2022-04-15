package com.github.aliwocha.taskmanager.dto.mapper;

import com.github.aliwocha.taskmanager.dto.request.TaskRequest;
import com.github.aliwocha.taskmanager.dto.response.TaskResponse;
import com.github.aliwocha.taskmanager.entity.Category;
import com.github.aliwocha.taskmanager.entity.Task;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.category.CategoryNotFoundException;
import com.github.aliwocha.taskmanager.exception.user.UserNotFoundException;
import com.github.aliwocha.taskmanager.repository.CategoryRepository;
import com.github.aliwocha.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskMapper {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TaskMapper(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public TaskResponse toDto(Task task) {
        TaskResponse taskResponse = new TaskResponse();

        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());

        if (task.getCategory() != null) {
            taskResponse.setCategory(CategoryMapper.toDto(task.getCategory()));
        }

        taskResponse.setPriority(task.getPriority());
        taskResponse.setStatus(task.getStatus());
        taskResponse.setDeadline(task.getDeadline());

        if (task.getUser() != null) {
            taskResponse.setUserId(task.getUser().getId());
        }

        return taskResponse;
    }

    public Task toEntity(TaskRequest taskRequest) {
        Task task = new Task();

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());

        Optional<Category> category = categoryRepository.findByNameIgnoreCase(taskRequest.getCategory());
        category.ifPresentOrElse(task::setCategory, CategoryNotFoundException::new);

        task.setPriority(taskRequest.getPriority());
        task.setStatus(taskRequest.getStatus());
        task.setDeadline(taskRequest.getDeadline());

        Optional<User> user = userRepository.findById(taskRequest.getUserId());
        user.ifPresentOrElse(task::setUser, UserNotFoundException::new);

        return task;
    }

    public Task updateEntity(Task task, TaskRequest taskRequest) {
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());

        Optional<Category> category = categoryRepository.findByNameIgnoreCase(taskRequest.getCategory());
        category.ifPresentOrElse(task::setCategory, CategoryNotFoundException::new);

        task.setPriority(taskRequest.getPriority());
        task.setStatus(taskRequest.getStatus());
        task.setDeadline(taskRequest.getDeadline());

        Optional<User> user = userRepository.findById(taskRequest.getUserId());
        user.ifPresentOrElse(task::setUser, UserNotFoundException::new);

        return task;
    }
}
