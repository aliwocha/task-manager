package com.github.aliwocha.taskmanager.exception.general;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Object ID does not match ID given in URL")
public class IdNotMatchingException extends RuntimeException {
}
