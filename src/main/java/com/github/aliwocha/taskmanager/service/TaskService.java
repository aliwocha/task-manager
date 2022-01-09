package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.dto.request.TaskRequest;
import com.github.aliwocha.taskmanager.dto.response.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskService {

    Page<TaskResponse> getTasksPaginated(Pageable pageable);

    Page<TaskResponse> getTasksByStatusPaginated(String status, Pageable pageable);

    Optional<TaskResponse> getTask(Long id);

    TaskResponse addTask(TaskRequest taskRequest);

    TaskResponse updateTask(TaskRequest taskRequest, Long id);

    void deleteTask(Long id);
}
