package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.dto.mapper.TaskMapper;
import com.github.aliwocha.taskmanager.dto.request.TaskRequest;
import com.github.aliwocha.taskmanager.dto.response.TaskResponse;
import com.github.aliwocha.taskmanager.entity.Category;
import com.github.aliwocha.taskmanager.entity.Task;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.category.CategoryNotFoundException;
import com.github.aliwocha.taskmanager.exception.task.InvalidTaskException;
import com.github.aliwocha.taskmanager.exception.task.TaskNotFoundException;
import com.github.aliwocha.taskmanager.exception.user.ForbiddenAccessException;
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

    private static final String ROLE_USER = "ROLE_USER";

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, CategoryRepository categoryRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public Page<TaskResponse> getTasks(User user, Pageable pageable) {
        Page<Task> taskPage = fetchTasks(user, pageable);
        List<TaskResponse> tasks = fetchTasks(user, pageable).getContent()
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(tasks, pageable, taskPage.getTotalElements());
    }

    @Override
    public Page<TaskResponse> getTasksByStatus(String status, User user, Pageable pageable) {
        List<String> statusNames = prepareStatusNamesList();
        if (!statusNames.contains(status.toUpperCase())) {
            throw new InvalidTaskException("Invalid status name");
        }

        Page<Task> taskPage = fetchTasksByStatus(status, user, pageable);
        List<TaskResponse> tasksByStatus = taskPage.getContent()
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(tasksByStatus, pageable, taskPage.getTotalElements());
    }

    @Override
    public Page<TaskResponse> getTasksByCategoryId(Long categoryId, User user, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        Page<Task> taskPage = fetchTasksByCategory(category, user, pageable);
        List<TaskResponse> tasksByCategory = taskPage.getContent()
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(tasksByCategory, pageable, taskPage.getTotalElements());
    }

    @Override
    public Optional<TaskResponse> getTask(Long id, User user) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && user.getRole().getName().equals(ROLE_USER)) {
            checkIfTaskBelongsToUser(user, task.get());
        }

        return task.map(taskMapper::toDto);
    }

    @Override
    public TaskResponse addTask(TaskRequest taskRequest, User user) {
        if (!user.getId().equals(taskRequest.getUserId())) {
            throw new ForbiddenAccessException();
        }

        checkIfDeadlineNotExpired(taskRequest);
        taskRequest.setStatus(Task.Status.NEW);

        return mapAndSaveTask(taskRequest);
    }

    @Override
    public TaskResponse updateTask(TaskRequest taskRequest, Long id, User user) {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);

        checkIfTaskBelongsToUser(user, task);
        checkIfDeadlineNotExpired(taskRequest);
        checkIfStatusNotOverdue(taskRequest);

        return updateAndSaveTask(task, taskRequest);
    }

    @Override
    public void deleteTask(Long id, User user) {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
        checkIfTaskBelongsToUser(user, task);
        taskRepository.deleteById(id);
    }

    private Page<Task> fetchTasks(User user, Pageable pageable) {
        if (user.getRole().getName().equals(ROLE_USER)) {
            return taskRepository.findAllByUser_Id(user.getId(), pageable);
        }
        return taskRepository.findAll(pageable);
    }

    private Page<Task> fetchTasksByStatus(String status, User user, Pageable pageable) {
        if (user.getRole().getName().equals(ROLE_USER)) {
            return taskRepository.findAllByStatusAndUser_Id(getStatus(status), user.getId(), pageable);
        }
        return taskRepository.findAllByStatus(getStatus(status), pageable);
    }

    private Page<Task> fetchTasksByCategory(Category category, User user, Pageable pageable) {
        if (user.getRole().getName().equals(ROLE_USER)) {
            return taskRepository.findAllByCategoryAndUser_Id(category, user.getId(), pageable);
        }
        return taskRepository.findAllByCategory(category, pageable);
    }

    private List<String> prepareStatusNamesList() {
        return Arrays.stream(Task.Status.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    private Task.Status getStatus(String status) {
        return Task.Status.valueOf(status.toUpperCase());
    }

    private void checkIfDeadlineNotExpired(TaskRequest taskRequest) {
        if (taskRequest.getDeadline() != null && taskRequest.getDeadline().isBefore(LocalDate.now())) {
            throw new InvalidTaskException("Date must be present or in the future");
        }
    }

    private void checkIfStatusNotOverdue(TaskRequest taskRequest) {
        if (taskRequest.getStatus() == Task.Status.OVERDUE) {
            throw new InvalidTaskException("Status cannot be set to 'OVERDUE'");
        }
    }

    private void checkIfTaskBelongsToUser(User user, Task task) {
        if (!user.getId().equals(task.getUser().getId())) {
            throw new ForbiddenAccessException();
        }
    }

    private TaskResponse mapAndSaveTask(TaskRequest taskRequest) {
        Task task = taskMapper.toEntity(taskRequest);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    private TaskResponse updateAndSaveTask(Task task, TaskRequest taskRequest) {
        task = taskMapper.updateEntity(task, taskRequest);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }
}
