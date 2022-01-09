package com.github.aliwocha.taskmanager.controller;

import com.github.aliwocha.taskmanager.dto.request.TaskRequest;
import com.github.aliwocha.taskmanager.dto.response.TaskResponse;
import com.github.aliwocha.taskmanager.service.impl.TaskServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
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
    public ResponseEntity<Page<TaskResponse>> getTasksPaginated(@RequestParam(required = false) String status,
                                                                @SortDefault(sort = "deadline")
                                                                @PageableDefault Pageable pageable) {
        if (status != null) {
            return ResponseEntity.ok(taskService.getTasksByStatusPaginated(status, pageable));
        } else {
            return ResponseEntity.ok(taskService.getTasksPaginated(pageable));
        }
    }

    @ApiOperation(value = "Get task by id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        return taskService.getTask(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Add task", notes = "Possible task priority: LOW, MEDIUM, HIGH.\n" + "Possible task status: NEW.\n" +
            "Provide deadline in format: yyyy-MM-dd.")
    @PostMapping
    public ResponseEntity<TaskResponse> addTask(@Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse savedTask = taskService.addTask(taskRequest);
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
    public ResponseEntity<TaskResponse> updateTask(@Valid @RequestBody TaskRequest taskRequest, @PathVariable Long id) {
        return ResponseEntity.ok(taskService.updateTask(taskRequest, id));
    }

    @ApiOperation(value = "Delete task by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponse> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
