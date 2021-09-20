package com.github.aliwocha.taskmanager.exception.email;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email already confirmed")
public class EmailAlreadyConfirmedException extends RuntimeException {
}
