package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.dto.response.ConfirmationTokenResponse;
import com.github.aliwocha.taskmanager.entity.User;

public interface AccountDetailsService {

    ConfirmationTokenResponse registerUser(User user);

    void enableUser(User user);

    ConfirmationTokenResponse resendConfirmationEmail(String login, String email);
}
