package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.dto.request.TaskRequest;
import com.github.aliwocha.taskmanager.dto.response.TaskResponse;
import com.github.aliwocha.taskmanager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskService {

    Page<TaskResponse> getTasksPaginated(User user, Pageable pageable);

    Page<TaskResponse> getTasksByStatusPaginated(String status, User user, Pageable pageable);

    Optional<TaskResponse> getTask(Long id, User user);

    TaskResponse addTask(TaskRequest taskRequest, User user);

    TaskResponse updateTask(TaskRequest taskRequest, Long taskId, User user);

    void deleteTask(Long id, User user);
}
