package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskService {

    Page<TaskDto> getTasksPaginated(Pageable pageable);

    Page<TaskDto> getTasksByStatusPaginated(String status, Pageable pageable);

    Optional<TaskDto> getTask(Long id);

    TaskDto addTask(TaskDto task);

    TaskDto updateTask(TaskDto task);

    void deleteTask(Long id);
}
