package com.github.aliwocha.taskmanager.exception.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Token expired")
public class TokenExpiredException extends RuntimeException {
}
