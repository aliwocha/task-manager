package com.github.aliwocha.taskmanager.service;

import com.github.aliwocha.taskmanager.dto.request.RegistrationRequest;
import com.github.aliwocha.taskmanager.dto.response.ConfirmationTokenResponse;

public interface RegistrationService {

    ConfirmationTokenResponse registerUser(RegistrationRequest registrationRequest);

    String confirmEmail(String token);

    ConfirmationTokenResponse resendConfirmationEmail(RegistrationRequest registrationRequest);
}
