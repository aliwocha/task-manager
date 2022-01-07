package com.github.aliwocha.taskmanager.controller;

import com.github.aliwocha.taskmanager.dto.request.UserRequest;
import com.github.aliwocha.taskmanager.dto.response.UserResponse;
import com.github.aliwocha.taskmanager.exception.general.IdForbiddenException;
import com.github.aliwocha.taskmanager.exception.general.IdNotMatchingException;
import com.github.aliwocha.taskmanager.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @ApiOperation(value = "Get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.getUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Add user", notes = "Possible roles: \"user\", \"admin\".\n" + "User id must not be provided.")
    @PostMapping
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserRequest userRequest) {
        if (userRequest.getId() != null) {
            throw new IdForbiddenException();
        }

        UserResponse savedUser = userService.addUser(userRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedUser);
    }

    @ApiOperation(value = "Update user", notes = "Possible roles: \"user\", \"admin\".")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest, @PathVariable Long id) {
        if (!id.equals(userRequest.getId())) {
            throw new IdNotMatchingException();
        }

        return ResponseEntity.ok(userService.updateUser(userRequest));
    }

    @ApiOperation(value = "Delete user by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
