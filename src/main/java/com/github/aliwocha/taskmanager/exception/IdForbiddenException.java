package com.github.aliwocha.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Saved object cannot have ID")
public class IdForbiddenException extends RuntimeException {
}
