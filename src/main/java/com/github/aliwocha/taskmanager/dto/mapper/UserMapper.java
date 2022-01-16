package com.github.aliwocha.taskmanager.dto.mapper;

import com.github.aliwocha.taskmanager.dto.request.UserRequest;
import com.github.aliwocha.taskmanager.dto.response.UserResponse;
import com.github.aliwocha.taskmanager.entity.Role;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.role.RoleNotFoundException;
import com.github.aliwocha.taskmanager.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Component
public class UserMapper {

    private final RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserResponse toDto(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setLogin(user.getLogin());
        userResponse.setEmail(user.getEmail());

        if (user.getRole() != null) {
            userResponse.setRole(user.getRole().getName());
        }

        userResponse.setEnabled(user.getEnabled());
        userResponse.setRegistrationDate(user.getRegistrationDate());

        return userResponse;
    }

    public User toEntity(UserRequest userRequest) {
        User user = new User();

        user.setLogin(userRequest.getLogin());
        user.setPassword(userRequest.getPassword());
        user.setEmail(userRequest.getEmail());
        user.setEnabled(false);
        user.setRegistrationDate(Timestamp.from(Instant.now()));

        Optional<Role> role = roleRepository.findByNameIgnoreCase(userRequest.getRole());
        role.ifPresentOrElse(user::setRole, RoleNotFoundException::new);

        return user;
    }

    public User updateEntity(User user, UserRequest userRequest) {
        user.setLogin(userRequest.getLogin());
        user.setPassword(userRequest.getPassword());
        user.setEmail(userRequest.getEmail());

        Optional<Role> role = roleRepository.findByNameIgnoreCase(userRequest.getRole());
        role.ifPresentOrElse(user::setRole, RoleNotFoundException::new);

        return user;
    }
}
