package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.dto.mapper.RegistrationMapper;
import com.github.aliwocha.taskmanager.dto.request.RegistrationRequest;
import com.github.aliwocha.taskmanager.entity.ConfirmationToken;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.email.EmailAlreadyConfirmedException;
import com.github.aliwocha.taskmanager.exception.token.TokenExpiredException;
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
    public String registerUser(RegistrationRequest registrationRequest) {
        User user = registrationMapper.toEntity(registrationRequest);
        return accountDetailsService.registerUser(user);
    }

    @Transactional
    @Override
    public String confirmEmail(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);
        checkIfEmailAlreadyConfirmed(confirmationToken);

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();
        checkIfTokenAlreadyExpired(expiresAt);

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenService.saveToken(confirmationToken);

        accountDetailsService.enableUser(confirmationToken.getUser());

        return String.format("Email '%s' confirmed", confirmationToken.getUser().getEmail());
    }

    private void checkIfEmailAlreadyConfirmed(ConfirmationToken confirmationToken) {
        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyConfirmedException();
        }
    }

    private void checkIfTokenAlreadyExpired(LocalDateTime expiresAt) {
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }
    }

    @Override
    public String resendConfirmationEmail(RegistrationRequest registrationRequest) {
        return accountDetailsService.resendConfirmationEmail(registrationRequest.getLogin(), registrationRequest.getEmail());
    }
}
