package com.github.aliwocha.taskmanager.exception.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Token not found")
public class TokenNotFoundException extends RuntimeException {
}
