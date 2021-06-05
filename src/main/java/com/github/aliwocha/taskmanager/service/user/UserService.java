package com.github.aliwocha.taskmanager.service.user;

import com.github.aliwocha.taskmanager.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> getAllUsers();

    Optional<UserDto> getUser(Long id);

    UserDto addUser(UserDto user);

    UserDto updateUser(UserDto user);

    void deleteUser(Long id);
}
