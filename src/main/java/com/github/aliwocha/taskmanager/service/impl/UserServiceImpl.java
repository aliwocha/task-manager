package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.dto.mapper.UserMapper;
import com.github.aliwocha.taskmanager.dto.request.UserRequest;
import com.github.aliwocha.taskmanager.dto.response.UserResponse;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.user.DuplicateUserException;
import com.github.aliwocha.taskmanager.exception.user.UserNotFoundException;
import com.github.aliwocha.taskmanager.repository.UserRepository;
import com.github.aliwocha.taskmanager.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponse> getUser(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        checkIfUserNotDuplicated(userRequest);
        return mapAndSaveUser(userRequest);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        checkIfUserNotDuplicated(userRequest, id);
        return updateAndSaveUser(user, userRequest);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }

    private void checkIfUserNotDuplicated(UserRequest userRequest) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(userRequest.getLogin());
        if (userByLogin.isPresent()) {
            throw new DuplicateUserException();
        }
    }

    private void checkIfUserNotDuplicated(UserRequest userRequest, Long id) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(userRequest.getLogin());
        userByLogin.ifPresent(user -> {
            if (!user.getId().equals(id)) {
                throw new DuplicateUserException();
            }
        });
    }

    private UserResponse mapAndSaveUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    private UserResponse updateAndSaveUser(User user, UserRequest userRequest) {
        user = userMapper.updateEntity(user, userRequest);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }
}
