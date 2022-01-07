package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.dto.mapper.TaskMapper;
import com.github.aliwocha.taskmanager.dto.request.TaskRequest;
import com.github.aliwocha.taskmanager.dto.response.TaskResponse;
import com.github.aliwocha.taskmanager.entity.Task;
import com.github.aliwocha.taskmanager.exception.category.CategoryNotFoundException;
import com.github.aliwocha.taskmanager.exception.task.InvalidTaskException;
import com.github.aliwocha.taskmanager.exception.task.TaskNotFoundException;
import com.github.aliwocha.taskmanager.repository.CategoryRepository;
import com.github.aliwocha.taskmanager.repository.TaskRepository;
import com.github.aliwocha.taskmanager.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CategoryRepository categoryRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<TaskResponse> getTasksPaginated(Pageable pageable) {
        Page<Task> taskPage = taskRepository.findAll(pageable);

        List<TaskResponse> tasks = taskPage.getContent()
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(tasks, pageable, taskPage.getTotalElements());
    }

    @Override
    public Page<TaskResponse> getTasksByStatusPaginated(String status, Pageable pageable) {
        List<String> statusNames = Arrays.stream(Task.Status.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        if (!statusNames.contains(status.toUpperCase())) {
            throw new InvalidTaskException("Invalid status name");
        }

        Page<Task> taskPage = taskRepository.findAll(pageable);

        List<TaskResponse> tasksByStatus = taskPage.getContent()
                .stream()
                .filter(t -> t.getStatus().equals(Task.Status.valueOf(status.toUpperCase())))
                .map(taskMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(tasksByStatus, pageable, taskPage.getTotalElements());
    }

    @Override
    public Optional<TaskResponse> getTask(Long id) {
        return taskRepository.findById(id).map(taskMapper::toDto);
    }

    @Override
    public TaskResponse addTask(TaskRequest task) {
        categoryRepository.findByNameIgnoreCase(task.getCategory())
                .orElseThrow(CategoryNotFoundException::new);

        if (task.getDeadline() != null && task.getDeadline().isBefore(LocalDate.now())) {
            throw new InvalidTaskException("Date must be present or in the future");
        }

        task.setStatus(Task.Status.NEW);
        return mapAndSaveTask(task);
    }

    @Override
    public TaskResponse updateTask(TaskRequest taskRequest) {
        categoryRepository.findByNameIgnoreCase(taskRequest.getCategory())
                .orElseThrow(CategoryNotFoundException::new);

        if (taskRequest.getDeadline() != null && taskRequest.getDeadline().isBefore(LocalDate.now())) {
            throw new InvalidTaskException("Date must be present or in the future");
        }

        if (taskRequest.getStatus() == Task.Status.OVERDUE) {
            throw new InvalidTaskException("Status cannot be set to 'OVERDUE'");
        }

        return mapAndSaveTask(taskRequest);
    }

    private TaskResponse mapAndSaveTask(TaskRequest taskRequest) {
        Task task = taskMapper.toEntity(taskRequest);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException();
        }

        taskRepository.deleteById(id);
    }
}
