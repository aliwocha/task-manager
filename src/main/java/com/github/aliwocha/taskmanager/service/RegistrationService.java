package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.dto.request.RegistrationRequest;

public interface RegistrationService {

    String registerUser(RegistrationRequest registrationRequest);

    String confirmEmail(String token);

    String resendConfirmationEmail(RegistrationRequest registrationRequest);
}
