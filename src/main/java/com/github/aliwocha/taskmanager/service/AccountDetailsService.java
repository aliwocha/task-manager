package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.entity.User;

public interface AccountDetailsService {

    void registerUser(User user);

    void enableUser(User user);
}
