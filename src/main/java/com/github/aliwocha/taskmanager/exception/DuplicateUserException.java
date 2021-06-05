package com.github.aliwocha.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User with given login already exists")
public class DuplicateUserException extends RuntimeException {
}
