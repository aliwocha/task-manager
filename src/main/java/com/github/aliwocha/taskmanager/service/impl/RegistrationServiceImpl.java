package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.dto.RegistrationDto;
import com.github.aliwocha.taskmanager.entity.ConfirmationToken;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.email.EmailAlreadyConfirmedException;
import com.github.aliwocha.taskmanager.exception.token.TokenExpiredException;
import com.github.aliwocha.taskmanager.mapper.RegistrationMapper;
import com.github.aliwocha.taskmanager.service.AccountDetailsService;
import com.github.aliwocha.taskmanager.service.ConfirmationTokenService;
import com.github.aliwocha.taskmanager.service.RegistrationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final AccountDetailsService accountDetailsService;
    private final ConfirmationTokenService confirmationTokenService;
    private final RegistrationMapper registrationMapper;

    public RegistrationServiceImpl(AccountDetailsService accountDetailsService, ConfirmationTokenService confirmationTokenService,
                                   RegistrationMapper registrationMapper) {
        this.accountDetailsService = accountDetailsService;
        this.confirmationTokenService = confirmationTokenService;
        this.registrationMapper = registrationMapper;
    }

    @Override
    public String registerUser(RegistrationDto registrationDto) {
        User user = registrationMapper.toEntity(registrationDto);
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

    @Override
    public String resendConfirmationEmail(RegistrationDto registrationDto) {
        return accountDetailsService.resendConfirmationEmail(registrationDto.getLogin(), registrationDto.getEmail());
    }
}
