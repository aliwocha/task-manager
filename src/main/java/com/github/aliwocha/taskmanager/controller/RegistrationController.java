package com.github.aliwocha.taskmanager.controller;

import com.github.aliwocha.taskmanager.dto.request.RegistrationRequest;
import com.github.aliwocha.taskmanager.service.RegistrationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @ApiOperation(value = "Register user", notes = "User id must not be provided.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(registrationService.registerUser(registrationRequest), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Resend confirmation email to user", notes = "User id must not be provided.")
    @PostMapping("/resend-email")
    public ResponseEntity<String> resendEmail(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(registrationService.resendConfirmationEmail(registrationRequest));
    }

    @ApiOperation(value = "Confirm email", notes = "Add param \"token\" to confirm registration.")
    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam @NotBlank String token) {
        return ResponseEntity.ok(registrationService.confirmEmail(token));
    }
}
