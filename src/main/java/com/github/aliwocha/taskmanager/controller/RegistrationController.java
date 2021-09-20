package com.github.aliwocha.taskmanager.controller;

import com.github.aliwocha.taskmanager.request.RegistrationRequest;
import com.github.aliwocha.taskmanager.service.RegistrationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest request) {
        return registrationService.registerUser(request);
    }

    @GetMapping("/confirm")
    public String confirmEmail(@RequestParam String token) {
        return registrationService.confirmEmail(token);
    }
}
