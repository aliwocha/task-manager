package com.github.aliwocha.taskmanager.exception.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Please confirm email. Token not expired yet")
public class TokenNotExpiredException extends RuntimeException {
}
