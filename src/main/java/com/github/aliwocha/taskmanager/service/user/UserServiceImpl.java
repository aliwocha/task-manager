package com.github.aliwocha.taskmanager.service.user;

import com.github.aliwocha.taskmanager.dto.UserDto;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.DuplicateUserException;
import com.github.aliwocha.taskmanager.exception.ResourceNotFoundException;
import com.github.aliwocha.taskmanager.mapper.UserMapper;
import com.github.aliwocha.taskmanager.repository.UserRepository;
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
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUser(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public UserDto addUser(UserDto user) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(user.getLogin());
        if (userByLogin.isPresent()) {
            throw new DuplicateUserException();
        }

        return mapAndSaveUser(user);
    }

    @Override
    public UserDto updateUser(UserDto user) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(user.getLogin());
        userByLogin.ifPresent(u -> {
            if (!u.getId().equals(user.getId())) {
                throw new DuplicateUserException();
            }
        });

        return mapAndSaveUser(user);
    }

    private UserDto mapAndSaveUser(UserDto user) {
        User userEntity = userMapper.toEntity(user);
        User savedUser = userRepository.save(userEntity);
        return userMapper.toDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }

        userRepository.deleteById(id);
    }
}
