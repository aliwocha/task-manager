package com.github.aliwocha.taskmanager.exception.general;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource with given ID does not exist")
public class ResourceNotFoundException extends RuntimeException {
}
