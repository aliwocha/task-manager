package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.dto.TaskDto;
import com.github.aliwocha.taskmanager.entity.Task;
import com.github.aliwocha.taskmanager.exception.task.InvalidTaskException;
import com.github.aliwocha.taskmanager.exception.general.ResourceNotFoundException;
import com.github.aliwocha.taskmanager.mapper.TaskMapper;
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
    public Page<TaskDto> getTasksPaginated(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);

        List<TaskDto> taskDtos = tasks.getContent()
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(taskDtos, pageable, tasks.getTotalElements());
    }

    @Override
    public Page<TaskDto> getTasksByStatusPaginated(String status, Pageable pageable) {
        List<String> statusNames = Arrays.stream(Task.Status.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        if (!statusNames.contains(status.toUpperCase())) {
            throw new InvalidTaskException("Requested status name does not exist");
        }

        Page<Task> tasks = taskRepository.findAll(pageable);

        List<TaskDto> taskDtosByStatus = tasks.getContent()
                .stream()
                .filter(t -> t.getStatus().equals(Task.Status.valueOf(status.toUpperCase())))
                .map(taskMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(taskDtosByStatus, pageable, tasks.getTotalElements());
    }

    @Override
    public Optional<TaskDto> getTask(Long id) {
        return taskRepository.findById(id).map(taskMapper::toDto);
    }

    @Override
    public TaskDto addTask(TaskDto task) {
        categoryRepository.findByNameIgnoreCase(task.getCategory())
                .orElseThrow(() -> new InvalidTaskException("Category with given name does not exist"));

        if (task.getDeadline() != null && task.getDeadline().isBefore(LocalDate.now())) {
            throw new InvalidTaskException("Date must be present or in the future");
        }

        task.setStatus(Task.Status.NEW);
        return mapAndSaveTask(task);
    }

    @Override
    public TaskDto updateTask(TaskDto task) {
        categoryRepository.findByNameIgnoreCase(task.getCategory())
                .orElseThrow(() -> new InvalidTaskException("Category with given name does not exist"));

        if (task.getDeadline() != null && task.getDeadline().isBefore(LocalDate.now())) {
            throw new InvalidTaskException("Date must be present or in the future");
        }

        if (task.getStatus() == Task.Status.OVERDUE) {
            throw new InvalidTaskException("Status cannot be set to 'OVERDUE'");
        }

        return mapAndSaveTask(task);
    }

    private TaskDto mapAndSaveTask(TaskDto task) {
        Task taskEntity = taskMapper.toEntity(task);
        Task savedTask = taskRepository.save(taskEntity);
        return taskMapper.toDto(savedTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }

        taskRepository.deleteById(id);
    }
}
