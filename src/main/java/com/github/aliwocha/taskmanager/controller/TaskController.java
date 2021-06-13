package com.github.aliwocha.taskmanager.controller;

import com.github.aliwocha.taskmanager.dto.TaskDto;
import com.github.aliwocha.taskmanager.exception.general.IdForbiddenException;
import com.github.aliwocha.taskmanager.exception.general.IdNotMatchingException;
import com.github.aliwocha.taskmanager.service.impl.TaskServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;

    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @ApiOperation(value = "Get all tasks paginated", notes = "Specify additional parameters to adjust page no., page size, offset etc.")
    @GetMapping
    public ResponseEntity<Page<TaskDto>> getTasksPaginated(@RequestParam(required = false) String status,
                                                           @PageableDefault Pageable pageable) {
        if (status != null) {
            return ResponseEntity.ok(taskService.getTasksByStatusPaginated(status, pageable));
        } else {
            return ResponseEntity.ok(taskService.getTasksPaginated(pageable));
        }
    }

    @ApiOperation(value = "Get task by id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        return taskService.getTask(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Add task", notes = "Possible task priority: LOW, MEDIUM, HIGH.\n" + "Possible task status: NEW.\n" +
            "Provide deadline in format: yyyy-MM-dd.\n" + "Task id must not be provided.")
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

    @ApiOperation(value = "Update task", notes = "Possible task priority: LOW, MEDIUM, HIGH.\n" + "Possible task status: " +
            "NEW, IN_PROGRESS, COMPLETED, OVERDUE.\n" + "Provide deadline in format: yyyy-MM-dd.")
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto task, @PathVariable Long id) {
        if (!id.equals(task.getId())) {
            throw new IdNotMatchingException();
        }

        return ResponseEntity.ok(taskService.updateTask(task));
    }

    @ApiOperation(value = "Delete task by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
