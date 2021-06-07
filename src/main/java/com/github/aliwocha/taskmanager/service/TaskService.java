package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.dto.TaskDto;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<TaskDto> getAllTasks();

    List<TaskDto> getAllTasksByStatus(String status);

    Optional<TaskDto> getTask(Long id);

    TaskDto addTask(TaskDto task);

    TaskDto updateTask(TaskDto task);

    void deleteTask(Long id);
}
