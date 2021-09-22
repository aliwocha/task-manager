package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.dto.RegistrationDto;

public interface RegistrationService {

    String registerUser(RegistrationDto registrationDto);

    String confirmEmail(String token);

    String resendConfirmationEmail(RegistrationDto registrationDto);
}
