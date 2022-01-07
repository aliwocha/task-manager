package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.dto.request.UserRequest;
import com.github.aliwocha.taskmanager.dto.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserResponse> getUsers();

    Optional<UserResponse> getUser(Long id);

    UserResponse addUser(UserRequest userRequest);

    UserResponse updateUser(UserRequest userRequest);

    void deleteUser(Long id);
}
