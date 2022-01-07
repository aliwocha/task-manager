package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.dto.mapper.UserMapper;
import com.github.aliwocha.taskmanager.dto.request.UserRequest;
import com.github.aliwocha.taskmanager.dto.response.UserResponse;
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
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(userRequest.getLogin());
        if (userByLogin.isPresent()) {
            throw new DuplicateUserException();
        }

        roleRepository.findByNameIgnoreCase(userRequest.getRole())
                .orElseThrow(RoleNotFoundException::new);

        return mapAndSaveUser(userRequest);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(userRequest.getLogin());
        userByLogin.ifPresent(u -> {
            if (!u.getId().equals(userRequest.getId())) {
                throw new DuplicateUserException();
            }
        });

        roleRepository.findByNameIgnoreCase(userRequest.getRole())
                .orElseThrow(RoleNotFoundException::new);

        return mapAndSaveUser(userRequest);
    }

    private UserResponse mapAndSaveUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }

        userRepository.deleteById(id);
    }
}
