package com.github.aliwocha.taskmanager.controller;

import com.github.aliwocha.taskmanager.dto.UserDto;
import com.github.aliwocha.taskmanager.exception.general.IdForbiddenException;
import com.github.aliwocha.taskmanager.exception.general.IdNotMatchingException;
import com.github.aliwocha.taskmanager.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @ApiOperation(value = "Get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return userService.getUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Add user", notes = "Possible roles: \"user\", \"admin\".\n" + "User id must not be provided.")
    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto user) {
        if (user.getId() != null) {
            throw new IdForbiddenException();
        }

        UserDto savedUser = userService.addUser(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedUser);
    }

    @ApiOperation(value = "Update user", notes = "Possible roles: \"user\", \"admin\".")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user, @PathVariable Long id) {
        if (!id.equals(user.getId())) {
            throw new IdNotMatchingException();
        }

        return ResponseEntity.ok(userService.updateUser(user));
    }

    @ApiOperation(value = "Delete user by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
