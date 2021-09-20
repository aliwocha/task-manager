package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.request.RegistrationRequest;

public interface RegistrationService {

    String registerUser(RegistrationRequest request);

    String confirmEmail(String token);
}
