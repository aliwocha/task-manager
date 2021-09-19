package com.github.aliwocha.taskmanager.mapper;

import com.github.aliwocha.taskmanager.dto.UserDto;
import com.github.aliwocha.taskmanager.entity.Role;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper {

    private final RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        if (user.getRole() != null) {
            dto.setRole(user.getRole().getName());
        }

        return dto;
    }

    public User toEntity(UserDto user) {
        User entity = new User();
        entity.setId(user.getId());
        entity.setLogin(user.getLogin());
        entity.setPassword(user.getPassword());
        entity.setEmail(user.getEmail());
        entity.setEnabled(false);
        Optional<Role> role = roleRepository.findByNameIgnoreCase(user.getRole());
        role.ifPresent(entity::setRole);

        return entity;
    }
}
