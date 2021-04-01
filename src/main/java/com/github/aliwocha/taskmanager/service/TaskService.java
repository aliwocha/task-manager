package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.api.dto.TaskDto;
import com.github.aliwocha.taskmanager.entity.Category;
import com.github.aliwocha.taskmanager.entity.Task;
import com.github.aliwocha.taskmanager.exception.InvalidTaskException;
import com.github.aliwocha.taskmanager.mapper.TaskMapper;
import com.github.aliwocha.taskmanager.repository.CategoryRepository;
import com.github.aliwocha.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;
    private CategoryRepository categoryRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.categoryRepository = categoryRepository;
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<TaskDto> getTask(Long id) {
        return taskRepository.findById(id).map(TaskMapper::toDto);
    }

    public TaskDto addTask(TaskDto task) {
        Optional<Category> categoryByName = categoryRepository.findByNameIgnoreCase(task.getCategory());
        if(categoryByName.isEmpty()) {
            throw new InvalidTaskException("Category with such name does not exist");
        }
        return mapAndSaveTask(task);
    }

    public TaskDto updateTask(TaskDto task) { // powtarza sie
        Optional<Category> categoryByName = categoryRepository.findByNameIgnoreCase(task.getCategory());
        if(categoryByName.isEmpty()) {
            throw new InvalidTaskException("Category with such name does not exist");
        }
        return mapAndSaveTask(task);
    }

    private TaskDto mapAndSaveTask(TaskDto task) {
        Task taskEntity = taskMapper.toEntity(task);
        Task savedTask = taskRepository.save(taskEntity);
        return TaskMapper.toDto(savedTask);
    }
}
