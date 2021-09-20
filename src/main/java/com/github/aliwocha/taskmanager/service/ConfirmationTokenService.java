package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.entity.ConfirmationToken;
import com.github.aliwocha.taskmanager.entity.User;

public interface ConfirmationTokenService {

    void saveToken(ConfirmationToken token);

    ConfirmationToken getToken(String token);

    ConfirmationToken createToken(User user);
}
