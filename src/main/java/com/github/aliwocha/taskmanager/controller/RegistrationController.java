package com.github.aliwocha.taskmanager.controller;

import com.github.aliwocha.taskmanager.request.RegistrationRequest;
import com.github.aliwocha.taskmanager.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String signUp(Model model) {
        model.addAttribute("request", new RegistrationRequest());
        return "register";
    }

    @PostMapping
    @ResponseBody
    public String registerUser(RegistrationRequest request) {
        return registrationService.registerUser(request);
    }

    @GetMapping("/confirm")
    @ResponseBody
    public String confirmEmail(@RequestParam String token) {
        return registrationService.confirmEmail(token);
    }
}
