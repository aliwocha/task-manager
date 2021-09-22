package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.entity.ConfirmationToken;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.token.TokenNotFoundException;
import com.github.aliwocha.taskmanager.repository.ConfirmationTokenRepository;
import com.github.aliwocha.taskmanager.service.ConfirmationTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public void saveToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    public ConfirmationToken getToken(String token) {
        return confirmationTokenRepository.findByTokenIgnoreCase(token).orElseThrow(TokenNotFoundException::new);
    }


    @Override
    public ConfirmationToken createToken(User user) {
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken();

        confirmationToken.setToken(token);
        LocalDateTime now = LocalDateTime.now();
        confirmationToken.setCreatedAt(now);
        confirmationToken.setExpiresAt(now.plusMinutes(2));
        confirmationToken.setUser(user);

        return confirmationToken;
    }

    @Override
    public boolean checkIfAllTokensExpired(User user) {
        return confirmationTokenRepository.findAllByUser(user)
                .stream()
                .allMatch(t -> t.getExpiresAt().isBefore(LocalDateTime.now()));
    }
}
