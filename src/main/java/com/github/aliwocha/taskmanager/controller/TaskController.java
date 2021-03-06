package com.github.aliwocha.taskmanager.controller;

import com.github.aliwocha.taskmanager.dto.request.TaskRequest;
import com.github.aliwocha.taskmanager.dto.response.TaskResponse;
import com.github.aliwocha.taskmanager.entity.AccountDetailsAdapter;
import com.github.aliwocha.taskmanager.service.impl.TaskServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @ApiOperation(value = "Get all tasks or tasks by status (optional)", notes = "Add param \"status\" to filter tasks by status. "
            + "Specify additional parameters to adjust page no., page size, offset etc.")
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getTasks(@RequestParam(required = false) String status,
                                                       @SortDefault(sort = "deadline")
                                                       @PageableDefault Pageable pageable) {
        AccountDetailsAdapter accountDetails = getAccountDetails();
        if (status != null) {
            return ResponseEntity.ok(taskService.getTasksByStatus(status, accountDetails.getUser(), pageable));
        } else {
            return ResponseEntity.ok(taskService.getTasks(accountDetails.getUser(), pageable));
        }
    }

    @ApiOperation(value = "Get tasks by category id")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<TaskResponse>> getTasksByCategoryId(@PathVariable Long categoryId,
                                                                   @SortDefault(sort = "deadline")
                                                                   @PageableDefault Pageable pageable) {
        AccountDetailsAdapter accountDetails = getAccountDetails();
        return ResponseEntity.ok(taskService.getTasksByCategoryId(categoryId, accountDetails.getUser(), pageable));
    }

    @ApiOperation(value = "Get task by id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        AccountDetailsAdapter accountDetails = getAccountDetails();
        return taskService.getTask(id, accountDetails.getUser())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Add task", notes = "Possible task priority: LOW, MEDIUM, HIGH.\n" + "Possible task status: NEW.\n" +
            "Provide deadline in format: yyyy-MM-dd.")
    @PostMapping
    public ResponseEntity<TaskResponse> addTask(@Valid @RequestBody TaskRequest taskRequest) {
        AccountDetailsAdapter accountDetails = getAccountDetails();
        TaskResponse savedTask = taskService.addTask(taskRequest, accountDetails.getUser());

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
        AccountDetailsAdapter accountDetails = getAccountDetails();
        return ResponseEntity.ok(taskService.updateTask(taskRequest, id, accountDetails.getUser()));
    }

    @ApiOperation(value = "Delete task by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        AccountDetailsAdapter accountDetails = getAccountDetails();
        taskService.deleteTask(id, accountDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    private AccountDetailsAdapter getAccountDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (AccountDetailsAdapter) authentication.getPrincipal();
    }
}
