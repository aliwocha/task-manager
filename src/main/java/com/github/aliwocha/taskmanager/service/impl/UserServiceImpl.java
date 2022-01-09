package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.dto.mapper.UserMapper;
import com.github.aliwocha.taskmanager.dto.request.UserRequest;
import com.github.aliwocha.taskmanager.dto.response.UserResponse;
import com.github.aliwocha.taskmanager.entity.Role;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.role.RoleNotFoundException;
import com.github.aliwocha.taskmanager.exception.user.DuplicateUserException;
import com.github.aliwocha.taskmanager.exception.user.UserNotFoundException;
import com.github.aliwocha.taskmanager.repository.RoleRepository;
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
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
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
        checkIfDuplicatedUser(userRequest);

        Optional<Role> roleByName = roleRepository.findByNameIgnoreCase(userRequest.getRole());
        if (roleByName.isEmpty()) {
            throw new RoleNotFoundException();
        }

        return mapAndSaveUser(userRequest);
    }

    private void checkIfDuplicatedUser(UserRequest userRequest) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(userRequest.getLogin());
        if (userByLogin.isPresent()) {
            throw new DuplicateUserException();
        }
    }

    private UserResponse mapAndSaveUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        checkIfDuplicatedUser(userRequest, id);

        Optional<Role> roleByName = roleRepository.findByNameIgnoreCase(userRequest.getRole());
        if (roleByName.isEmpty()) {
            throw new RoleNotFoundException();
        }

        return updateAndSaveUser(user, userRequest, roleByName.get());
    }

    private void checkIfDuplicatedUser(UserRequest userRequest, Long id) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(userRequest.getLogin());
        userByLogin.ifPresent(user -> {
            if (!user.getId().equals(id)) {
                throw new DuplicateUserException();
            }
        });
    }

    private UserResponse updateAndSaveUser(User user, UserRequest userRequest, Role role) {
        user.setLogin(userRequest.getLogin());
        user.setPassword(userRequest.getPassword());
        user.setEmail(userRequest.getEmail());
        user.setRole(role);

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }

        userRepository.deleteById(id);
    }
}
