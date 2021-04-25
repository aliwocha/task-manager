package com.github.aliwocha.taskmanager.service.task;

import com.github.aliwocha.taskmanager.dto.TaskDto;
import com.github.aliwocha.taskmanager.entity.Task;
import com.github.aliwocha.taskmanager.exception.InvalidTaskException;
import com.github.aliwocha.taskmanager.exception.ResourceNotFoundException;
import com.github.aliwocha.taskmanager.mapper.TaskMapper;
import com.github.aliwocha.taskmanager.repository.CategoryRepository;
import com.github.aliwocha.taskmanager.repository.TaskRepository;
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

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getAllTasksByStatus(String status) {
        List<String> statusNames = Arrays.stream(Task.Status.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        if (!statusNames.contains(status.toUpperCase())) {
            throw new InvalidTaskException("Requested status name does not exist");
        }

        return taskRepository.findAllByStatus(Task.Status.valueOf(status.toUpperCase()))
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<TaskDto> getTask(Long id) {
        return taskRepository.findById(id).map(taskMapper::toDto);
    }

    public TaskDto addTask(TaskDto task) {
        categoryRepository.findByNameIgnoreCase(task.getCategory())
                .orElseThrow(() -> new InvalidTaskException("Category with given name does not exist"));

        if (task.getDeadline() != null && task.getDeadline().isBefore(LocalDate.now())) {
            throw new InvalidTaskException("Date must be present or in the future");
        }

        task.setStatus(Task.Status.NEW);
        return mapAndSaveTask(task);
    }

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

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }

        taskRepository.deleteById(id);
    }
}
