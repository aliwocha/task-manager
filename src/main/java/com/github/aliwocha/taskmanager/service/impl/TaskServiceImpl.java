package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.dto.mapper.TaskMapper;
import com.github.aliwocha.taskmanager.dto.request.TaskRequest;
import com.github.aliwocha.taskmanager.dto.response.TaskResponse;
import com.github.aliwocha.taskmanager.entity.Category;
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
        List<String> statusNames = prepareStatusNamesList();
        if (!statusNames.contains(status.toUpperCase())) {
            throw new InvalidTaskException("Invalid status name");
        }

        Page<Task> taskPage = taskRepository.findAll(pageable);
        List<TaskResponse> tasksByStatus = filterTasksByStatus(status, taskPage);

        return new PageImpl<>(tasksByStatus, pageable, taskPage.getTotalElements());
    }

    private List<String> prepareStatusNamesList() {
        return Arrays.stream(Task.Status.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    private List<TaskResponse> filterTasksByStatus(String status, Page<Task> taskPage) {
        return taskPage.getContent()
                .stream()
                .filter(task -> task.getStatus().equals(Task.Status.valueOf(status.toUpperCase())))
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TaskResponse> getTask(Long id) {
        return taskRepository.findById(id).map(taskMapper::toDto);
    }

    @Override
    public TaskResponse addTask(TaskRequest taskRequest) {
        Optional<Category> categoryByName = categoryRepository.findByNameIgnoreCase(taskRequest.getCategory());
        if (categoryByName.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        checkIfDeadlineExpired(taskRequest);

        taskRequest.setStatus(Task.Status.NEW);
        return mapAndSaveTask(taskRequest);
    }

    private TaskResponse mapAndSaveTask(TaskRequest taskRequest) {
        Task task = taskMapper.toEntity(taskRequest);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    public TaskResponse updateTask(TaskRequest taskRequest, Long id) {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);

        Optional<Category> categoryByName = categoryRepository.findByNameIgnoreCase(taskRequest.getCategory());
        if (categoryByName.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        checkIfDeadlineExpired(taskRequest);
        checkIfStatusOverdue(taskRequest);

        return updateAndSaveTask(task, taskRequest, categoryByName.get());
    }

    private void checkIfDeadlineExpired(TaskRequest taskRequest) {
        if (taskRequest.getDeadline() != null && taskRequest.getDeadline().isBefore(LocalDate.now())) {
            throw new InvalidTaskException("Date must be present or in the future");
        }
    }

    private void checkIfStatusOverdue(TaskRequest taskRequest) {
        if (taskRequest.getStatus() == Task.Status.OVERDUE) {
            throw new InvalidTaskException("Status cannot be set to 'OVERDUE'");
        }
    }

    private TaskResponse updateAndSaveTask(Task task, TaskRequest taskRequest, Category category) {
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setCategory(category);
        task.setPriority(taskRequest.getPriority());
        task.setStatus(taskRequest.getStatus());
        task.setDeadline(taskRequest.getDeadline());

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException();
        }

        taskRepository.deleteById(id);
    }
}
