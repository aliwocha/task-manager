package com.github.aliwocha.taskmanager.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User with given login not found")
public class UserLoginNotFoundException extends RuntimeException {
}
