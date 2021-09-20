package com.github.aliwocha.taskmanager.exception.email;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email already taken")
public class DuplicateEmailException extends RuntimeException {
}
