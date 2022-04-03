package com.github.aliwocha.taskmanager.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User not allowed to perform this action")
public class ForbiddenAccessException extends RuntimeException {
}
