package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.entity.Role;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.repository.RoleRepository;
import com.github.aliwocha.taskmanager.request.RegistrationRequest;
import com.github.aliwocha.taskmanager.service.AccountDetailsService;
import com.github.aliwocha.taskmanager.service.RegistrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final AccountDetailsService accountDetailsService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(AccountDetailsService accountDetailsService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.accountDetailsService = accountDetailsService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(RegistrationRequest request) {
        // uzyc mappera RegistrationMapper
        User user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setEnabled(false);
        Optional<Role> role = roleRepository.findByNameIgnoreCase("ROLE_USER");
        role.ifPresent(user::setRole);

        return accountDetailsService.register(user);
    }
}
