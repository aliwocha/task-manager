package com.github.aliwocha.taskmanager.mapper;

import com.github.aliwocha.taskmanager.dto.RegistrationDto;
import com.github.aliwocha.taskmanager.entity.Role;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RegistrationMapper {

    private static final String DEFAULT_USER_ROLE = "ROLE_USER";

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public RegistrationMapper(PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public RegistrationDto toDto(User user) {
        RegistrationDto dto = new RegistrationDto();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());

        return dto;
    }

    public User toEntity(RegistrationDto registrationDto) {
        User user = new User();
        user.setLogin(registrationDto.getLogin());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        user.setEnabled(false);
        Optional<Role> role = roleRepository.findByNameIgnoreCase(DEFAULT_USER_ROLE);
        role.ifPresent(user::setRole);

        return user;
    }
}
