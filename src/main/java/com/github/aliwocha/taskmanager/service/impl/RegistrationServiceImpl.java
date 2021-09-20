package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.entity.ConfirmationToken;
import com.github.aliwocha.taskmanager.entity.Role;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.email.EmailAlreadyConfirmedException;
import com.github.aliwocha.taskmanager.exception.token.TokenExpiredException;
import com.github.aliwocha.taskmanager.repository.RoleRepository;
import com.github.aliwocha.taskmanager.request.RegistrationRequest;
import com.github.aliwocha.taskmanager.service.AccountDetailsService;
import com.github.aliwocha.taskmanager.service.ConfirmationTokenService;
import com.github.aliwocha.taskmanager.service.RegistrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final AccountDetailsService accountDetailsService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    public RegistrationServiceImpl(AccountDetailsService accountDetailsService, RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService) {
        this.accountDetailsService = accountDetailsService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public String registerUser(RegistrationRequest request) {
        // uzyc mappera RegistrationMapper
        User user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setEnabled(false);
        Optional<Role> role = roleRepository.findByNameIgnoreCase("ROLE_USER");
        role.ifPresent(user::setRole);

        return accountDetailsService.registerUser(user);
    }

    @Transactional
    @Override
    public String confirmEmail(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);

        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyConfirmedException();
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenService.saveToken(confirmationToken);

        accountDetailsService.enableUser(confirmationToken.getUser());

        return String.format("Email '%s' confirmed", confirmationToken.getUser().getEmail());
    }
}
