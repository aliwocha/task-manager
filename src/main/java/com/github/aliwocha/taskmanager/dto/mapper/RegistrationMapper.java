package com.github.aliwocha.taskmanager.dto.mapper;

import com.github.aliwocha.taskmanager.dto.request.RegistrationRequest;
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

    public User toEntity(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setLogin(registrationRequest.getLogin());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setEnabled(false);
        user.setRegistrationDate(registrationRequest.getRegistrationDate());
        Optional<Role> role = roleRepository.findByNameIgnoreCase(DEFAULT_USER_ROLE);
        role.ifPresent(user::setRole);

        return user;
    }
}
