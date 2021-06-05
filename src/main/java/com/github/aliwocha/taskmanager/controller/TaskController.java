package com.github.aliwocha.taskmanager.controller;

import com.github.aliwocha.taskmanager.dto.TaskDto;
import com.github.aliwocha.taskmanager.exception.IdForbiddenException;
import com.github.aliwocha.taskmanager.exception.IdNotMatchingException;
import com.github.aliwocha.taskmanager.service.task.TaskServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;

    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDto> getAllTasks(@RequestParam(required = false) String status) {
        if (status != null) {
            return taskService.getAllTasksByStatus(status);
        } else {
            return taskService.getAllTasks();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        return taskService.getTask(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaskDto> addTask(@RequestBody TaskDto task) {
        if (task.getId() != null) {
            throw new IdForbiddenException();
        }

        TaskDto savedTask = taskService.addTask(task);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTask.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto task, @PathVariable Long id) {
        if (!id.equals(task.getId())) {
            throw new IdNotMatchingException();
        }

        TaskDto updatedTask = taskService.updateTask(task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
