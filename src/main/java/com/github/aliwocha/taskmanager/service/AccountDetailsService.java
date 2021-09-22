package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.entity.User;

public interface AccountDetailsService {

    String registerUser(User user);

    void enableUser(User user);

    String resendConfirmationEmail(String login, String email);
}
